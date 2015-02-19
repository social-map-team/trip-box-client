package com.socialmap.yy.travelbox.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DailyTravelSchedule {
    private List<TravelSchedule> schedules = new LinkedList<>();
    private Date date;
    private String title;

    public List<TravelSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<TravelSchedule> schedules) {
        this.schedules = schedules;
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
