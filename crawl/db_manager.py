#encoding:utf-8
'''
pymysql封装
'''
import pymysql
import datetime
from crawl.db_config import localSourceConfig as localConfig

class db_manage(object):
    def __init__(self, config=localConfig):
        self.db = pymysql.connect(host=config['host'],port=config['port'],user=config['user'],
                                      passwd=config['passwd'],db=config['db'],charset=config['charset'],
                                      cursorclass=config['cursorclass'])
        self.cursor = self.db.cursor()
        self.cursor.execute("SELECT VERSION()")
        data = self.cursor.fetchone()

    def insertConf(self, confer, link, catagories, time, place, ddl):
        if time == 'N/A':
            start_time = 'N/A'
            end_time = 'N/A'
        else:
            rs = time.split(' - ')
            start_time = datetime.datetime.strptime(rs[0], '%b %d, %Y').date()
            end_time = datetime.datetime.strptime(rs[1], '%b %d, %Y').date()
        if ddl == 'N/A':
            ddl = 'N/A'
        else:
            ddl = datetime.datetime.strptime(ddl, '%b %d, %Y').date()
        cate = " | ".join(str(i) for i in catagories)
        self.cursor.execute("insert into conference(conference, link,categories,start_time,end_time,location,submission_deadline)"
                            " values(%s, %s, %s, %s, %s, %s, %s)",
                            (confer, link, cate, start_time, end_time, place, ddl))
        self.db.commit()
        return True

