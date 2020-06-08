# 学术日历与地图

## 实现功能

- [x] 用户管理，包括登录、注册、邮件找回密码、修改密码、用户中心，权限控制等
- [x] 会议管理，包括会议名搜索，分类搜索，会议详情，会议信息反馈等
- [x] 用户收藏会议功能，包括会议收藏，删除收藏等
- [x] Google Map，Google Calendar调用，以及保存会议日程到本地
- [x] ElasticSearch全文检索会议信息部分
- [x] 基于Kafka的消息通知功能
- [x] 基于Redis和Echarts的网站统计功能

## 技术栈

* SpringBoot + MyBatis + MYSQL + Google API
* 具体技术选型：
  * 中间件：Kafka，Redis，Elasticsearch
  * 前端：bootstrap，Jquery，ajax，Thymeleaf，Echarts
  * 后端：Spring，SpringBoot，SpringMVC，MyBatis
  * 数据库：MYSQL，Redis



## 测试时运行说明
- 运行说明
  - 先运行SQL文件，建立必备数据库，同时需要安装好Redis
  - 程序中application.properties中修改相关配置，主要是修改数据库连接相关
  - 运行中间件Kafka，Elasticsearch（使用6.x版本）
  - 运行 SpringBoot 程序，使用 localhost:8080 即可访问
- 数据源获取
  - 仓库中的SQL脚本含有数据，若要自己爬取，运行crawl/crawl_core.py，环境是Python3.7，依赖库有bs4和lxml



## 待办：

- [ ] 部分死链和静态页面设计不完善的地方，以及页面美化
- [ ] 改善架构，实现高可用（目前完成了分布式缓存优化）




## 项目成员

* [jonnyS](https://github.com/JonnyS1226)（爬虫，前端，后端）
* [Clairejfj](https://github.com/Clairejfj)（后端，测试）
* [dasklarleo](https://github.com/dasklarleo)（爬虫，前端）

