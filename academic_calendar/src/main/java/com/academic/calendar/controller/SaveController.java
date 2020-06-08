package com.academic.calendar.controller;

import com.academic.calendar.entity.User;
import com.academic.calendar.service.SaveService;
import com.academic.calendar.util.CommonUtils;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SaveController {

    @Autowired
    private UserHolder userHolder;
    @Autowired
    private SaveService saveService;

    // 用户保存会议, ajax异步请求
    @RequestMapping(path = "/store", method = RequestMethod.POST)
    @ResponseBody
    public String storeConference(int conferenceId) {
        User user = userHolder.getUser();
        int i = saveService.saveConference(user.getUserId(), conferenceId);
        return CommonUtils.getJSONString(0, "添加收藏成功");
    }

    // 用户删除保存的会议， ajax异步请求
    @RequestMapping(path = "/clear", method = RequestMethod.POST)
    @ResponseBody
    public String clearConference(int conferenceId) {
        User user = userHolder.getUser();
        saveService.removeConference(user.getUserId(), conferenceId);
        return CommonUtils.getJSONString(0, "取消收藏成功");
    }

    private boolean hasAdded(int userId, int conferenceId) {
        return saveService.isAdded(userId, conferenceId);
    }

}
