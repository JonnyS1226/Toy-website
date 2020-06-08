package com.academic.calendar.controller.interceptor;

import com.academic.calendar.entity.User;
import com.academic.calendar.service.NoticeService;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class NoticeInterceptor implements HandlerInterceptor {
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private NoticeService noticeService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = userHolder.getUser();
        int unreadRows = 0;
        if (user != null && modelAndView != null) {
            unreadRows = noticeService.findUnreadRows(user.getUserId());
            modelAndView.addObject("unreadRows", unreadRows);
        }
    }
}
