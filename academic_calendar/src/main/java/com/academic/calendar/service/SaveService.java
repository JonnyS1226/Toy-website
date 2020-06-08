package com.academic.calendar.service;

import com.academic.calendar.dao.ConferenceDao;
import com.academic.calendar.dao.SaveDao;
import com.academic.calendar.entity.Conference;
import com.academic.calendar.entity.ConferenceES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户保存会议 业务
 */
@Service
public class SaveService {

    @Autowired
    private SaveDao saveDao;
    @Autowired
    private ConferenceDao conferenceDao;

    // 用户保存会议
    public int saveConference(int userId, int conferenceId) {
        return saveDao.insertSave(userId, conferenceId);
    }

    // 是否有记录
    public boolean isAdded(int userId, int conferenceId) {
        int ans = saveDao.selectConference(userId, conferenceId);
        if (ans > 0) {
            return true;
        } else {
            return false;
        }
    }

    // 用户删除会议
    public int removeConference(int userId, int conferenceId) {
        return saveDao.deleteSave(userId, conferenceId);
    }

    // 用户搜索已收藏的会议
    public List<Conference> findConferenceByUser(int userId, int offset, int limit) {
        List<Integer> list = saveDao.selectConferenceByUser(userId, offset, limit);
        List<Conference> ans = new ArrayList<>();
        for (Integer integer : list) {
            Conference conference = conferenceDao.selectConferenceById(integer);
            ans.add(conference);
        }
        return ans;
    }

    // 查询记录条数
    public int findSaveRows(int userId) {
        return saveDao.selectSaveRows(userId);
    }

    // 根据会议id查用户
    public List<Integer> findUser(int conferenceId) {
        return saveDao.selectUserByConference(conferenceId);
    }
}
