package com.socialmap.yy.travelbox.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DailyTravelSchedule {
    private List<ScheduleEvent> events = new LinkedList<>();
    private Date date;
    private String title;

    public List<ScheduleEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ScheduleEvent> events) {
        this.events = events;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
