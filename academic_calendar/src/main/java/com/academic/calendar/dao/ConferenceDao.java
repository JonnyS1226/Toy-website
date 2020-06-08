package com.academic.calendar.dao;

import com.academic.calendar.entity.Conference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 涉及到动态sql，所以这里不采用注解方式了，采用xml更清晰
 */
@Mapper
public interface ConferenceDao {

    // 手动录入会议
    int insertConference(Conference conference);

    // 根据种类查询
    List<Conference> selectConferenceByCate(@Param("category") String category, int offset, int limit);

    // 根据会议名精确查询
    Conference selectConferenceByName(@Param("conference") String conference);

    // 根据会议名模糊查询
    List<Conference> selectAllConferenceByName(@Param("conference") String conference, int offset, int limit);

    // 查询所有
    List<Conference> selectAllConference(int offset, int limit);

    // 根据ID查询
    Conference selectConferenceById(int id);

    // 查询会议条数，动态拼条件
    int selectConferenceRows(@Param("conference")String conference, @Param("category")String category);

    // 更新会议
    int updateConference(Conference conference);

    int deleteConference(int id);

}
