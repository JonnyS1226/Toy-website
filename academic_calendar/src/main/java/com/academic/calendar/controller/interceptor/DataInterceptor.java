package com.academic.calendar.controller.interceptor;

import com.academic.calendar.entity.User;
import com.academic.calendar.service.DataService;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DataInterceptor implements HandlerInterceptor {

    @Autowired
    private DataService dataService;
    @Autowired
    private UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 统计UV
        String ip = request.getRemoteHost();
        dataService.recordIP(ip);
        // 统计DAU
        User user = userHolder.getUser();
        if (user != null) {
            dataService.recordDAU(user.getUserId());
        }
        return true;
    }
}
