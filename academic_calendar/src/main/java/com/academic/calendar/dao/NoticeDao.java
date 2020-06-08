package com.academic.calendar.dao;

import com.academic.calendar.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeDao {

    @Insert(
            "insert into notice(conference_id, type, to_id, status) "
            + "values(#{conferenceId}, #{type}, #{toId}, #{status})"
    )
    @Options(keyProperty = "id", useGeneratedKeys = true)
    int insertNotice(Notice notice);

    @Select(
            "select * from notice "
            + "where to_id=#{userId} "
            + "order by create_time desc "
            + "limit #{offset}, #{limit}"
    )
    List<Notice> selectNotice(int userId, int offset, int limit);

    @Update(
            "update notice "
            + "set status = 1 where id=#{id}"
    )
    int updateNotice(int id);

    @Select(
            "select count(*) from notice "
            + "where to_id=#{userId} "
            + "and status = 0 "
    )
    int selectUnreadRows(int userId);

    @Select(
            "select count(*) from notice "
                    + "where to_id=#{userId} "
    )
    int selectRows(int userId);

    @Delete("delete from notice where id = #{id}")
    int deleteNotice(int id);
}
