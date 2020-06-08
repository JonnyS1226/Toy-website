package com.academic.calendar.service;

import com.academic.calendar.dao.ConferenceDao;
import com.academic.calendar.entity.Conference;
import com.academic.calendar.entity.ConferenceES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会议相关业务层
 */
@Service
public class ConferenceService {

    @Autowired
    private ConferenceDao conferenceDao;

    // 添加会议
    public Map<String, Object> addConference(Conference conference) {
        Map<String, Object> map = new HashMap<>();
        Conference conf = conferenceDao.selectConferenceByName(conference.getConference());
        if (conf != null) {
            map.put("msg", "该会议已存在，请直接检索~");
            return map;
        }
        int ans = conferenceDao.insertConference(conference);
        if (ans != 1) {
            map.put("msg", "录入会议失败，请检查连接~");
            return map;
        }
        return map;
    }

    // 检索全部会议
    public List<Conference> findConference(int offset, int limit) {
        return conferenceDao.selectAllConference(offset, limit);
    }

    // 查询会议记录数
    public int findConferenceRows(String conference, String category) {
        return conferenceDao.selectConferenceRows(conference, category);
    }

    // 根据名称检索
    public List<Conference> findConferenceByName(String name, int offset, int limit) {
        return conferenceDao.selectAllConferenceByName(name, offset, limit);
    }

    // 根据类别检索
    public List<Conference> findConferenceByCate(String category, int offset, int limit) {
        return conferenceDao.selectConferenceByCate(category, offset, limit);
    }


    // 根据id查询会议
    public Conference findConferenceById (int id) {
        return conferenceDao.selectConferenceById(id);
    }

    // 根据id更新会议
    public int editConferenceById(Conference conference) {
        return conferenceDao.updateConference(conference);
    }

    public int deleteConference(int id) {
        return conferenceDao.deleteConference(id);
    }
}
