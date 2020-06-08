package com.academic.calendar.controller.interceptor;


import com.academic.calendar.entity.LoginTicket;
import com.academic.calendar.entity.User;
import com.academic.calendar.service.NoticeService;
import com.academic.calendar.service.UserService;
import com.academic.calendar.util.CommonUtils;
import com.academic.calendar.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 处理是否已登录 的拦截器
 */
@Component
public class AfterLoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserHolder userHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;

    //控制器执行前，获取user(方法是先从cookie中获取ticket，再根据ticket获取user)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CommonUtils.getValueFromCookie(request, "ticket");
        if (ticket != null){
            //2. 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //3. 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 1 && loginTicket.getExpired().after(new Date())){
                //4. 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                //5. 在本次请求中持有用户，即存user。但要在多线程隔离中存放，避免多线程发送冲突
                userHolder.setUser(user);
                //6. 构建用户认证的结果，并存入SecurityContext，以便于Security进行授权
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getUserId())
                );
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
        return true;
    }

    //该方法在执行器之后，模板之前运行，默认有modelandview对象，于是可以用来给模板装user
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = userHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("alreadyLoginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
