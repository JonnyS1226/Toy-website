package com.academic.calendar.entity;

import java.util.Date;

/**
 * 通知实体类
 */
public class Notice {

    private int id;
    private int conferenceId;
    private String type;
    private int toId;
    private int status;
    private Date createTime;

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", conferenceId=" + conferenceId +
                ", type='" + type + '\'' +
                ", toId=" + toId +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
