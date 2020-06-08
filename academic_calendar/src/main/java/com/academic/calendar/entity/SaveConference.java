package com.academic.calendar.entity;

import java.util.Date;

/**
 * 用户保存会议信息
 */
public class SaveConference {

    private int saveId;
    private int userId;
    private int conferenceId;
    private Date insertTime;

    @Override
    public String toString() {
        return "SaveConference{" +
                "saveId=" + saveId +
                ", userId=" + userId +
                ", conferenceId=" + conferenceId +
                ", insertTime=" + insertTime +
                '}';
    }

    public int getSaveId() {
        return saveId;
    }

    public void setSaveId(int saveId) {
        this.saveId = saveId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}
