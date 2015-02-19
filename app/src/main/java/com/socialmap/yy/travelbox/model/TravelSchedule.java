package com.socialmap.yy.travelbox.model;

import java.util.Date;


public class TravelSchedule {
    private Date start;
    private Date end;
    private String title;
    private String content;

    public TravelSchedule() {

    }

    public TravelSchedule(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
