package com.team_comfortable.forhealthylife.ui.calendar.model;

import com.team_comfortable.forhealthylife.ui.calendar.util.DateUtil;

import java.util.Calendar;

public class Day extends ViewModel
{
    private String day;
    public Day()
    {
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }

    // TODO : day에 달력일값넣기
    public void setCalendar(Calendar calendar) {
        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);
    }
}

