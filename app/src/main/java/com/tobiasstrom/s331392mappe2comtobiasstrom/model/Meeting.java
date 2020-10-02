package com.tobiasstrom.s331392mappe2comtobiasstrom.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Meeting {

    private String meeting_start;
    private String meeting_end;
    private String meeting_date;
    private String meeting_place;
    private String meeting_type;
    private int metingId;

    public Meeting() {
    }

    public Meeting(String meeting_start, String meeting_end, String meeting_place, String meeting_type, int metingId) {
        this.meeting_start = meeting_start;
        this.meeting_end = meeting_end;
        this.meeting_place = meeting_place;
        this.meeting_type = meeting_type;
        this.metingId = metingId;
    }

    public String getMeeting_start() {
        return meeting_start;
    }

    public void setMeeting_start(String meeting_start) {
        this.meeting_start = meeting_start;
    }

    public String getMeeting_end() {
        return meeting_end;
    }

    public void setMeeting_end(String meeting_end) {
        this.meeting_end = meeting_end;
    }

    public String getMeeting_place() {
        return meeting_place;
    }

    public void setMeeting_place(String meeting_place) {
        this.meeting_place = meeting_place;
    }

    public String getMeeting_type() {
        return meeting_type;
    }

    public void setMeeting_type(String meeting_type) {
        this.meeting_type = meeting_type;
    }

    public int getMetingId() {
        return metingId;
    }

    public void setMetingId(int metingId) {
        this.metingId = metingId;
    }
}
