package com.academic.calendar.entity;

/**
 * 会议信息实体类
 */
public class Conference {
    private int id;
    private String conference;
    private String link;
    private String categories;
    private String startTime;
    private String endTime;
    private String location;
    private String submissionDeadline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(String submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    @Override
    public String toString() {
        return "Conference{" +
                "id=" + id +
                ", conference='" + conference + '\'' +
                ", link='" + link + '\'' +
                ", categories='" + categories + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", location='" + location + '\'' +
                ", submissionDeadline='" + submissionDeadline + '\'' +
                '}';
    }
}
