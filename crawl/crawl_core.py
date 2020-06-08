# encoding:utf-8
'''
多线程 + xpath + 正则匹配 进行爬虫
1. 先爬取候选url，形成urlList
2. 根据urlList去爬取会议的具体数据
'''
from crawl.db_manager import db_manage
from bs4 import BeautifulSoup
import requests
import re
from lxml import etree
import threading

threadLock = threading.Lock()
# 页码
page = 1


def getUrlList(pageIndex):
    '''获取候选url（候选会议详情页）'''
    url = "http://www.wikicfp.com/cfp/call?conference=computer%20science&page=" + str(pageIndex)
    try:
        r = requests.get(url, timeout=10)
    except requests.exceptions.RequestException as e:
        print(e)
    html = r.text
    bs = BeautifulSoup(html, "html.parser")
    t_list1 = bs.find_all(attrs={"bgcolor":"#e6e6e6"})
    t_list2 = bs.find_all(attrs={"bgcolor":"#f6f6f6"})
    pattern = re.compile(r'href="(.*?)"')
    u1 = pattern.findall(str(t_list1))
    u2 = pattern.findall(str(t_list2))
    urlList = []
    for i in u1:
        urlList.append('http://www.wikicfp.com' + i)
    for j in u2:
        urlList.append('http://www.wikicfp.com' + j)
    return urlList


def getMainPage(url):
    '''获取要爬取的会议详细页'''
    user_agent = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36'
    headers = {'User-Agent': user_agent}
    html = None
    try:
        html = requests.get(url, headers=headers, timeout=10).text
    except requests.exceptions.RequestException as e:
        print(e)
    if html:
        return html
    else:
        return None


def getItemCore(html):
    '''获取会议相关具体信息'''
    # confer_name, conf_series, link, Categories
    conferpa = re.compile(
        '<span property="v:description"> (.*?)</span>')
    linkpa = re.compile('Link: <a.*?>(.*?)</a>')

    # conference,link
    confer = re.findall(conferpa, html)
    link = re.findall(linkpa, html)
    if link == []:
        link.append('null')

    # catagories
    selector = etree.HTML(html)
    categories = selector.xpath('/html/body/div/center/table/tr/td/table/tr/td/table/tr/td/table/tr/td/h5/a/text()')

    # when,where,ddl
    pattern = re.compile('<th>When</th>.*?<td align="center">.*?(\\S.*?)\\n.*?</td>.*?' +
                         '<th>Where</th>.*?<td align="center">(.*?)</td>.*?' +
                         '<th>Submission Deadline</th>.*?<span property="v:startDate" content=.*?>(.*?)</span>', re.S)
    items = re.findall(pattern, html)

    for item in items:
        dbm = db_manage()
        dbm.insertConf(confer[0], link[0], categories, item[0], item[1], item[2])



def run():
    '''线程任务'''
    threadLock.acquire()
    global page
    print(threading.currentThread().name)
    urlList = []
    urlList = getUrlList(page)
    page += 1
    for url in urlList:
        html = getMainPage(url)
        if html:
            getItemCore(html)
    threadLock.release()


def main():
    threadList = []
    for i in range(30):
        thread = threading.Thread(target=run)
        thread.start()
        threadList.append(thread)
    for t in threadList:
        t.join()
    print("完成")


if __name__ == '__main__':
    while(True):
        main()
