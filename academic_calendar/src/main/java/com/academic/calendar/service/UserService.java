package com.academic.calendar.service;

import com.academic.calendar.dao.LoginTicketDao;
import com.academic.calendar.dao.UserDao;
import com.academic.calendar.entity.LoginTicket;
import com.academic.calendar.entity.User;
import com.academic.calendar.util.CommonUtils;
import com.academic.calendar.util.Constant;
import com.academic.calendar.util.MailUtil;
import com.academic.calendar.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户业务层
 */
@Service
public class UserService implements Constant {
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginTicketDao loginTicketDao;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MailUtil mailUtil;

    //注册业务
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("invalid parameters");
        }
        if (StringUtils.isBlank(user.getUsername()))
        {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword()))
        {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail()))
        {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        //判断账号和邮箱是否已存在
        User tmp = userDao.selectByName(user.getUsername());
        if (tmp != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }
        tmp = userDao.selectByEmail(user.getEmail());
        if (tmp != null) {
            map.put("emailMsg", "该邮箱已被注册");
            return map;
        }
        //无已存在 开始注册
        user.setSalt(CommonUtils.generateUUID().substring(0, 5));
        user.setPassword(CommonUtils.md5(user.getPassword() + user.getSalt()));
        user.setRole(0);
        userDao.insertUser(user);
        return map;
    }

    // 登录业务
    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();
        //1. 空值处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if (user == null){
            user = userDao.selectByEmail(username);
            if (user == null) {
                map.put("usernameMsg", "该账号或邮箱不存在");
                return map;
            }
        }
        // 验证密码
        password =CommonUtils.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg", "密码错误");
            return map;
        }
        //登录成功，录入凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getUserId());
        loginTicket.setTicket(CommonUtils.generateUUID());
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
//        loginTicketDao.insertLoginTicket(loginTicket);
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);         //redis会把对象序列化为json字符串保存
        map.put("ticket", loginTicket.getTicket());
        return map;
    }


    //退出登录，status置0
    public void logout(String ticket){
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(0);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    // 查询loginticket，简单来说就是作为业务层中转
    public LoginTicket findLoginTicket(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    // 查询User，使用redis优化
    public User findUserById(int id) {
//        return userDao.selectById(id);
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    // 修改密码，忘记密码的接口
    public Map<String, Object> editPassword(int userId, String oldPwd, String newPwd) {
        Map<String, Object> map = new HashMap<>();
//        User user = userDao.selectById(userId);
        User user = getCache(userId);
        if (user == null) {
            user = initCache(userId);
        }
        System.out.println(oldPwd + "," + newPwd);
        oldPwd = CommonUtils.md5(oldPwd + user.getSalt());
        if (user.getPassword().equals(oldPwd)) {
            newPwd = CommonUtils.md5(newPwd + user.getSalt());
            int i = userDao.modifyPassword(newPwd, userId);
            clearCache(userId);
            if (i == 1) {
                map.put("msg", "修改密码成功");
            } else {
                map.put("msg", "修改密码失败");
            }
        } else {
            map.put("passwordMsg", "原密码不匹配");
        }
        return map;
    }

    // 查询所有用户
    public List<User> findAllUser() {
        return userDao.selectAllUser();
    }

    // 找回密码-发送邮件
    public Map<String, Object> sendForgetMail(String email) {
        Map<String, Object> map = new HashMap<>();
        Context context = new Context();
        User user = userDao.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "该邮箱未被注册");
            return map;
        }
        context.setVariable("email", email);
        String code = CommonUtils.generateUUID().substring(0, 6);
        context.setVariable("code", code);
        // 验证码存入redis，有效期60s
        String redisKey = RedisKeyUtil.getCodeKey(email);
        redisTemplate.opsForValue().set(redisKey, code, 60, TimeUnit.SECONDS);
        // 发邮件
        String content = templateEngine.process("/mail/forget", context);
        mailUtil.sendMail(email, "找回密码", content);
        return map;
    }

    // 找回密码
    public Map<String, Object> forgetPwd(String email, String password, String verification) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(verification)) {
            map.put("codeMsg", "验证码不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        User user = userDao.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "该邮箱未被注册");
            return map;
        }
        // 验证码比对
        String redisKey = RedisKeyUtil.getCodeKey(email);
        String code = (String) redisTemplate.opsForValue().get(redisKey);
        if (code == null || !code.equals(verification)) {
            map.put("codeMsg", "验证码不正确或已过期");
            return map;
        }
        // 修改密码
        password = CommonUtils.md5(password + user.getSalt());
        int i = userDao.modifyPassword(password, user.getUserId());
        clearCache(user.getUserId());
        if (i != 1) {
            map.put("passwordMsg", "修改密码失败");
            return map;
        }
        return map;
    }

    // 缓存用户信息 ，先查缓存，缓存查不到再查mysql
    // 1. 优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }
    // 2. 取不到时就初始化缓存数据，取到则直接返回
    private User initCache(int userId) {
        User user = userDao.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }
    // 3. 数据变更时，清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

    // 对用户权限做适配
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getRole()) {
                    case 0: return AUTHORITY_USER;
                    case 1: return AUTHORITY_ADMIN;
                }
                throw new IllegalArgumentException("unknown authority");
            }
        });
        return list;
    }
}
