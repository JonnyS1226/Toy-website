package com.academic.calendar.service;

import com.academic.calendar.dao.NoticeDao;
import com.academic.calendar.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知 业务层
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;

    // 查询通知
    public List<Notice> findNotice(int userId, int offset, int limit) {
        return noticeDao.selectNotice(userId, offset, limit);
    }

    // 已读通知
    public int readNotice(int id) {
        return noticeDao.updateNotice(id);
    }

    // 未读数量
    public int findUnreadRows(int userId) {
        return noticeDao.selectUnreadRows(userId);
    }

    // 总数
    public int findRows(int userId) {
        return noticeDao.selectRows(userId);
    }

    // 删除通知
    public int deleteNotice(int id) {
        return noticeDao.deleteNotice(id);
    }

    // 添加通知
    public int addNotice(Notice notice) {
        return noticeDao.insertNotice(notice);
    }

}
