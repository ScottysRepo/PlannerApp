package com.example.sseethoff.plannerapp;

//this is the model

import java.sql.Time;
import java.util.Date;
import java.util.UUID;
import java.util.GregorianCalendar;

public class Plan {

    private UUID mId;
    private String mActivity;
    private Date mDate;
    private Time mTime;
    private String mLocation;

    public Plan() {
        this(UUID.randomUUID());
    }

    public Plan(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getActivity() {
        return mActivity;
    }

    public void setActivity(String activity) {
        mActivity = activity;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }
}