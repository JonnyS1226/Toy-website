package com.academic.calendar.entity;

/**
 * 发布事件
 */
public class Event {

    private int conferenceId;
    private String topic;

    @Override
    public String toString() {
        return "Event{" +
                "conferenceId=" + conferenceId +
                ", topic='" + topic + '\'' +
                '}';
    }

    public int getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(int conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
