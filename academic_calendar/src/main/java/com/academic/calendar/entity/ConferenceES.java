package com.academic.calendar.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 会议信息实体类 ES用    坑点：es需要的实体类和springboot不能用一个
 */
@Document(indexName = "academic_calendar_map", type = "_doc", shards = 6, replicas = 3)
public class ConferenceES {
    @Id
    private int id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String conference;
    @Field(type = FieldType.Text)
    private String link;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String categories;
    @Field(type = FieldType.Text)
    private String startTime;
    @Field(type = FieldType.Text)
    private String endTime;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String location;
    @Field(type = FieldType.Text)
    private String submissionDeadline;

    // 提供一个由ORM实体转变为ES实体的构造方法---这也是需要注意的点，ES实体不能用springmvc进行整体注入
    public ConferenceES(Conference conference) {
        if (conference != null) {
            this.setId(conference.getId());
            this.setConference(conference.getConference());
            this.setCategories(conference.getCategories());
            this.setStartTime(conference.getStartTime());
            this.setEndTime(conference.getEndTime());
            this.setLocation(conference.getLocation());
            this.setLink(conference.getLink());
            this.setSubmissionDeadline(conference.getSubmissionDeadline());
        }
    }

    public ConferenceES(){}

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
