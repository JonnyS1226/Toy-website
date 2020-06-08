package com.academic.calendar.controller;

import com.academic.calendar.entity.User;
import com.academic.calendar.service.DataService;
import com.academic.calendar.service.NoticeService;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 审计和统计 控制器
 */
@Controller
public class DataController {
    @Autowired
    private DataService dataService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserHolder userHolder;


    // 统计页面
    @RequestMapping(path = "/data", method = {RequestMethod.GET, RequestMethod.POST})
    public String getDataPage(Model model) {
        User user = userHolder.getUser();
        int unreadRows = 0;
        if (user != null) {
            unreadRows = noticeService.findUnreadRows(user.getUserId());
        }
        model.addAttribute("unreadRows", unreadRows);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dates = new ArrayList<>();
        List<Long> dauDatas = new ArrayList<>();
        List<Long> uvDatas = new ArrayList<>();
        for (int i = 6; i >= 0; --i) {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -i);
            date = (Date) calendar.getTime();
            dates.add(df.format(date));
            dauDatas.add(dataService.calculateDAU(date, date));
            uvDatas.add(dataService.calculateUV(date, date));
        }
        model.addAttribute("dates", dates);
        model.addAttribute("dauDatas", dauDatas);
        model.addAttribute("uvDatas", uvDatas);
        return "data";
    }

    // 统计UV
    @RequestMapping(path = "/data/uv", method = RequestMethod.POST)
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model) {
        long uv = dataService.calculateUV(start, end);
        model.addAttribute("uvResult", uv);
        model.addAttribute("uvStartDate", start);
        model.addAttribute("uvEndDate", end);
        return "forward:/data";
    }

    // 统计DAU
    @RequestMapping(path = "/data/dau", method = RequestMethod.POST)
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, Model model) {
        long dau = dataService.calculateDAU(start, end);
        model.addAttribute("dauResult", dau);
        model.addAttribute("dauStartDate", start);
        model.addAttribute("dauEndDate", end);
        return "forward:/data";
    }

}
