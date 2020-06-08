package com.academic.calendar.util;

/**
 * 常量接口
 */
public interface Constant {

    /**
     * 事件：更新会议
     */
    String TOPIC_UPDATE_CONFERENCE = "update";

    /**
     * 事件：添加会议
     */
    String TOPIC_INSERT_CONFERENCE = "insert";

    /**
     * 事件：转存Elasticsearch
     */
    String TOPIC_STORE_ES = "store";

    /**
     * 用户权限：普通用户
     */
    String AUTHORITY_USER = "user";

    /**
     * 用户权限：管理员
     */
    String AUTHORITY_ADMIN = "admin";

    /**
     * 消息未读
     */
    int NOTICE_UNREAD = 0;

    /**
     * 消息已读
     */
    int NOTICE_READ = 1;
}
