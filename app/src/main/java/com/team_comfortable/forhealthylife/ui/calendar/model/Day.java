package com.team_comfortable.forhealthylife.ui.calendar.model;

import com.team_comfortable.forhealthylife.ui.calendar.util.DateUtil;

import java.util.Calendar;

public class Day extends ViewModel
{
    private String day,date;
    
    public Day()
    {
    }

    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String day) {
        this.date = day;
    }

    public void setCalendar(Calendar calendar) {
        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
        date = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DATE_FORMAT);
    }
}

