package com.academic.calendar.event;

import com.academic.calendar.dao.ConferenceDao;
import com.academic.calendar.entity.*;
import com.academic.calendar.service.ElasticsearchService;
import com.academic.calendar.service.NoticeService;
import com.academic.calendar.service.SaveService;
import com.academic.calendar.service.UserService;
import com.academic.calendar.util.Constant;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventConsumer implements Constant {
    // MQ消费事件
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private SaveService saveService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private ConferenceDao conferenceDao;
    @Autowired
    private ElasticsearchService elasticsearchService;

    // MQ处理事件
    @KafkaListener(topics = {TOPIC_INSERT_CONFERENCE, TOPIC_UPDATE_CONFERENCE})
    public void handleMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("empty message");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("wrong pattern");
            return;
        }

        // 构建通知
        Notice notice = new Notice();
        notice.setType(event.getTopic());
        notice.setStatus(NOTICE_UNREAD);
        notice.setConferenceId(event.getConferenceId());
        if (event.getTopic().equals(TOPIC_INSERT_CONFERENCE)) {
            List<User> allUser = userService.findAllUser();
            for (User user : allUser) {
                notice.setToId(user.getUserId());
                noticeService.addNotice(notice);
            }
        } else if (event.getTopic().equals(TOPIC_UPDATE_CONFERENCE)) {
            List<Integer> ids = saveService.findUser(event.getConferenceId());
            for (Integer id : ids) {
                notice.setToId(id);
                noticeService.addNotice(notice);
            }
        }
    }

    // 当用户新增，修改时，都要更新，ES处理事件
    @KafkaListener(topics = {TOPIC_STORE_ES})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("empty message");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("wrong pattern");
            return;
        }
        Conference conference = conferenceDao.selectConferenceById(event.getConferenceId());
        ConferenceES conferenceES = new ConferenceES(conference);
        elasticsearchService.saveConference(conferenceES);
    }
}
