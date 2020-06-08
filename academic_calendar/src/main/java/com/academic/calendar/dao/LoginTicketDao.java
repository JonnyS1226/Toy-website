package com.academic.calendar.dao;

import com.academic.calendar.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * 登录凭证Dao，专门服务于登录操作
 */
@Mapper
public interface LoginTicketDao {

    //登录时生成登录凭证，status=1为登录，status=0为退出
    @Insert(
            "insert into login_ticket(user_id,ticket,status,expired) "+
            "values(#{userId},#{ticket},#{status},#{expired})"
    )
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    //根据ticket查询整个凭证
    @Select("select id, user_id, ticket, status, expired "
            + "from login_ticket where ticket=#{ticket}"
    )
    LoginTicket selectByTicket(String ticket);

    //退出失效，修改状态
    @Update("update login_ticket set status=#{status} where ticket=#{ticket}")
    int updateStatus(String ticket, int status);


    // 定时清理一批不在登录状态的登录凭证
    @Delete("delete from login_ticket where status=0")
    int deleteTicket();

}
