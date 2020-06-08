package com.academic.calendar.controller;

import com.academic.calendar.entity.Conference;
import com.academic.calendar.entity.Notice;
import com.academic.calendar.entity.Page;
import com.academic.calendar.entity.User;
import com.academic.calendar.service.ConferenceService;
import com.academic.calendar.service.NoticeService;
import com.academic.calendar.util.CommonUtils;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private ConferenceService conferenceService;


    // 展示消息
    @RequestMapping(path = "/notice/show", method = RequestMethod.GET)
    public String getNoticePage(Model model, Page page) {
        User user = userHolder.getUser();
        // 分页信息
        page.setLimit(10);
        page.setPath("/notice/show");
        page.setRows(noticeService.findRows(user.getUserId()));
        List<Notice> notices = noticeService.findNotice(user.getUserId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeList = new ArrayList<>();
        if (notices != null) {
            for (Notice notice : notices) {
                Map<String, Object> map = new HashMap<>();
                map.put("notice", notice);
                map.put("user", user);
                Conference conferenceById = conferenceService.findConferenceById(notice.getConferenceId());
                map.put("conf", conferenceById);
                noticeList.add(map);
            }
        }
        model.addAttribute("noticeList", noticeList);
        int unreadRows = 0;
        unreadRows = noticeService.findUnreadRows(user.getUserId());
        model.addAttribute("unreadRows", unreadRows);
        return "notice";
    }

    // 跳转并已读
    @RequestMapping(path = "/notice/read/{noticeId}/{conferenceId}", method = RequestMethod.GET)
    public String getNoticeRead(@PathVariable("noticeId") int noticeId, @PathVariable("conferenceId") int conferenceId) {
        int i = noticeService.readNotice(noticeId);
        return "forward:/conference/detail/" + conferenceId;
    }

    // 删除消息
    @RequestMapping(path = "/notice/delete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteNotice(int noticeId) {
        int i = noticeService.deleteNotice(noticeId);
        return CommonUtils.getJSONString(0, "通知删除成功");
    }

}
