package com.academic.calendar.dao;

import com.academic.calendar.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户相关操作dao
 */
@Mapper
public interface UserDao {

    @Insert("insert into user(username,password,salt,email,role) " +
            "values(#{username},#{password},#{salt},#{email},#{role})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(User user);

    @Select("select user_id,username,password,salt,email,role,create_time from user " +
            "where username=#{username}")
    User selectByName(String username);

    @Select("select user_id,username,password,salt,email,role,create_time from user " +
            "where email=#{email}")
    User selectByEmail(String email);

    @Select("select user_id,username,password,salt,email,role,create_time from user " +
            "where user_id=#{id}")
    User selectById(int id);

    @Update(
            "update user set password=#{password} "
            + "where user_id = #{userId}"
    )
    int modifyPassword(String password, int userId);

    @Select("select * from user")
    List<User> selectAllUser();

}
