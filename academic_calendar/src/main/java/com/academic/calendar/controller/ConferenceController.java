package com.academic.calendar.controller;


import com.academic.calendar.entity.Conference;
import com.academic.calendar.entity.Event;
import com.academic.calendar.entity.Page;
import com.academic.calendar.entity.User;
import com.academic.calendar.event.EventProducer;
import com.academic.calendar.service.ConferenceService;
import com.academic.calendar.service.NoticeService;
import com.academic.calendar.service.SaveService;
import com.academic.calendar.util.Constant;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会议检索 添加 控制器
 */
@RequestMapping(path = "/conference")
@Controller
public class ConferenceController implements Constant {

    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private SaveService saveService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private NoticeService noticeService;

    // 获取查询会议界面
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String getSearchConferencePage(Model model, Page page){
        page.setRows(conferenceService.findConferenceRows(null, null));
        page.setPath("/conference/search");
        List<Conference> list = conferenceService.findConference(page.getOffset(), page.getLimit());

        List<Map<String, Object>> confInfo = new ArrayList<>();
        if (list != null) {
            for (Conference conf : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("conference", conf.getConference());
                map.put("startTime", conf.getStartTime());
                map.put("endTime", conf.getEndTime());
                map.put("location", conf.getLocation());
                map.put("link", conf.getLink());
                map.put("conferenceId", conf.getId());
                confInfo.add(map);
            }
        }
        model.addAttribute("conference", confInfo);
        User user = userHolder.getUser();
        int unreadRows = 0;
        if (user != null) {
            unreadRows = noticeService.findUnreadRows(user.getUserId());
        }
        model.addAttribute("unreadRows", unreadRows);
        return "conference";
    }

    // 获取添加会议页面
    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public String getAddConferencePage(Model model) {
        User user = userHolder.getUser();
        int unreadRows = 0;
        if (user != null) {
            unreadRows = noticeService.findUnreadRows(user.getUserId());
        }
        model.addAttribute("unreadRows", unreadRows);
        return "add.html";
    }

    // 手动录入会议
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String doAddConference(Model model, Conference conference) {
        Map<String, Object> map = new HashMap<>();
        map = conferenceService.addConference(conference);
        // 发布通知
        Event event = new Event();
        event.setTopic(TOPIC_INSERT_CONFERENCE);
        event.setConferenceId(conference.getId());
        eventProducer.fireEvent(event);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "录入会议成功");
            // 转存ES
            event.setTopic(TOPIC_STORE_ES);
            eventProducer.fireEvent(event);
            return "add";
        } else {
            model.addAttribute("msg", map.get("msg"));
            return "add";
        }
    }

    // 获取会议详情界面
    @RequestMapping(path = "/detail/{conferenceId}", method = RequestMethod.GET)
    public String getConferenceDetailPage(@PathVariable("conferenceId") int conferenceId ,Model model) {
        // 查询会议详情
        Conference conference = conferenceService.findConferenceById(conferenceId);
        User user = userHolder.getUser();
        if (user != null) {
            model.addAttribute("hasAdded", hasAdded(user.getUserId(), conferenceId));
        } else {
            model.addAttribute("hasAdded", false);
        }
        model.addAttribute("conferenceInfo", conference);
        model.addAttribute("user", userHolder.getUser());
        int unreadRows = 0;
        if (user != null) {
            unreadRows = noticeService.findUnreadRows(user.getUserId());
        }
        model.addAttribute("unreadRows", unreadRows);
        return "detail";
    }

    // 根据名称查询会议  /conference/search/name?keyword=xxx
    @RequestMapping(path = "/search/name", method = RequestMethod.GET)
    public String searchConferenceByName(Model model, Page page, String keyword) {
        List<Conference> conferenceByName =
                conferenceService.findConferenceByName("%" + keyword + "%", page.getOffset(), page.getLimit());
        List<Map<String, Object>> confInfo = new ArrayList<>();
        if (conferenceByName != null) {
            for (Conference conf : conferenceByName) {
                Map<String, Object> map = new HashMap<>();
                map.put("conference", conf.getConference());
                map.put("startTime", conf.getStartTime());
                map.put("endTime", conf.getEndTime());
                map.put("location", conf.getLocation());
                map.put("link", conf.getLink());
                map.put("conferenceId", conf.getId());
                confInfo.add(map);
            }
        }
        page.setRows(conferenceService.findConferenceRows("%" + keyword + "%", null));
        page.setPath("/conference/search/name?keyword=" + keyword);
        model.addAttribute("conference", confInfo);
        User user = userHolder.getUser();
        int unreadRows = 0;
        if (user != null) {
            unreadRows = noticeService.findUnreadRows(user.getUserId());
        }
        model.addAttribute("unreadRows", unreadRows);
        return "conference";
    }

    // 根据类型查询会议  /conference/search/category
    @RequestMapping(path = "/search/{category}", method = RequestMethod.GET)
    public String searchConferenceByCate(Model model, Page page, @PathVariable("category") String category) {
        page.setRows(conferenceService.findConferenceRows(null, "%" + category + "%"));
        page.setPath("/conference/search/" + category);
        List<Conference> conferenceByCate =
                conferenceService.findConferenceByCate("%" + category + "%", page.getOffset(), page.getLimit());
        List<Map<String, Object>> confInfo = new ArrayList<>();
        if (conferenceByCate != null) {
            for (Conference conf : conferenceByCate) {
                Map<String, Object> map = new HashMap<>();
                map.put("conference", conf.getConference());
                map.put("startTime", conf.getStartTime());
                map.put("endTime", conf.getEndTime());
                map.put("location", conf.getLocation());
                map.put("link", conf.getLink());
                map.put("conferenceId", conf.getId());
                confInfo.add(map);
            }
        }
        model.addAttribute("conference", confInfo);
        User user = userHolder.getUser();
        int unreadRows = 0;
        if (user != null) {
            unreadRows = noticeService.findUnreadRows(user.getUserId());
        }
        model.addAttribute("unreadRows", unreadRows);
        return "conference";
    }

    // 修改会议
    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String editConference(Conference conference, Model model) {
        int i = conferenceService.editConferenceById(conference);
        // 查询会议详情
        conference = conferenceService.findConferenceById(conference.getId());
        User user = userHolder.getUser();
        if (user != null) {
            model.addAttribute("hasAdded", hasAdded(user.getUserId(), conference.getId()));
        } else {
            model.addAttribute("hasAdded", false);
        }
        model.addAttribute("conferenceInfo", conference);
        model.addAttribute("user", userHolder.getUser());
        model.addAttribute("conferenceId", conference.getId());
        // 发布通知
        Event event = new Event();
        event.setConferenceId(conference.getId());
        event.setTopic(TOPIC_UPDATE_CONFERENCE);
        eventProducer.fireEvent(event);
        // 转存ES
        event.setTopic(TOPIC_STORE_ES);
        eventProducer.fireEvent(event);
        return "detail";
    }

    // 是否加入了收藏
    private boolean hasAdded(int userId, int conferenceId) {
        return saveService.isAdded(userId, conferenceId);
    }

}
