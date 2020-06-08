package com.academic.calendar.controller;

import com.academic.calendar.entity.User;
import com.academic.calendar.service.NoticeService;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.awt.desktop.QuitEvent;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 首页控制器
 */
@Controller
public class HomeController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserHolder userHolder;

    // 进入主页
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getHomePage(Model model){
        User user = userHolder.getUser();
        int unreadRows = 0;
        if (user != null) {
            unreadRows = noticeService.findUnreadRows(user.getUserId());
        }
        model.addAttribute("unreadRows", unreadRows);
        return "index";
    }

    // 进入关于我们，涉及锚点，需要重定向
    @RequestMapping(path = "/aboutUs", method = RequestMethod.GET)
    public String getAboutUsPage() {
        return "redirect:/index#aboutus";
    }

    // 权限不足
    @RequestMapping(path = "denied", method = RequestMethod.GET)
    public String getDenied() {
        return "/error/404";
    }
}
