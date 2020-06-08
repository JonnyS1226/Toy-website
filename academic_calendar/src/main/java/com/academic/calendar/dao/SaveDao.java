package com.academic.calendar.dao;

import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * 用户保存会议 Dao
 */
@Mapper
public interface SaveDao {

    @Select(
            "select user_id from save_conference " +
            "where conference_id = #{conferenceId}"
    )
    List<Integer> selectUserByConference(int conferenceId);

    @Select(
            "select conference_id from save_conference "
            + "where user_id = #{userId} "
            + "limit #{offset}, #{limit}"
    )
    List<Integer> selectConferenceByUser(int userId, int offset, int limit);

    @Select(
            "select count(*) from save_conference "
            + "where user_id = #{userId} and conference_id = #{conferenceId}"
    )
    int selectConference(int userId, int conferenceId);

    @Select(
            "select insert_time from save_conference "
            + "where user_id = #{userId}"
    )
    Date selectTimeByUser(int userId);

    @Insert(
            "insert into save_conference(user_id, conference_id) "
            + "values(#{userId}, #{conferenceId})"
    )
    int insertSave(int userId, int conferenceId);

    @Delete(
            "delete from save_conference "
            + "where user_id = #{userId} and conference_id = #{conferenceId}"
    )
    int deleteSave(int userId, int conferenceId);

    @Select(
            "select count(*) from save_conference "
            + "where user_id=#{userId}"
    )
    int selectSaveRows(int userId);
}
