package com.academic.calendar;


import com.academic.calendar.dao.UserDao;
import com.academic.calendar.entity.User;
import com.academic.calendar.util.CommonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 用户接口测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AcadCalMapApplication.class)
public class UserDaoTests {

    @Autowired
    UserDao userDao;

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("admin2ads3");
        user.setPassword("admin23");
        user.setSalt(CommonUtils.generateUUID().substring(0, 5));
        user.setEmail("12d34@12dd34.com");
//        user.setCreateTime(new Date());
        userDao.insertUser(user);
        System.out.println(user.getUserId());
    }

    @Test
    public void testSelectUser() {
        System.out.println(userDao.selectByEmail("123@123.com"));
        System.out.println(userDao.selectByName("admin"));
    }
}
