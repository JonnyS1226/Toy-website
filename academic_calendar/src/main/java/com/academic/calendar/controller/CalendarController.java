package com.academic.calendar.controller;

import com.academic.calendar.entity.Conference;
import com.academic.calendar.service.CalendarService;
import com.academic.calendar.service.ConferenceService;
import com.academic.calendar.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 关于谷歌日历和本地ics的控制层
 */
@Controller
public class CalendarController {

    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private CalendarService calendarService;

    // 添加到谷歌日历
    @RequestMapping(path = "/googlecalendar", method = RequestMethod.POST)
    @ResponseBody
    public String addEventToGoogle(int cid1) {
        Conference conferenceById = conferenceService.findConferenceById(cid1);
        calendarService.addEventToGoogleCalendar(conferenceById);
        return CommonUtils.getJSONString(0, "添加到Google Calendar成功");
    }

    // 添加到本地 (生成ics)
    @RequestMapping(path = "/local", method = RequestMethod.POST)
    @ResponseBody
    public String addEventToLocal(int cid2) {
        Conference conferenceById = conferenceService.findConferenceById(cid2);
        try {
            calendarService.grabEvents(conferenceById);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CommonUtils.getJSONString(0, "添加到本地成功");
    }


}
