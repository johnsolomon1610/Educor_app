package com.example.educor_app.Assignment;

public class assignment_data {
    private String class_name,name,due_Time,date,description,userid,url;

    public assignment_data() {
    }

    public assignment_data(String name, String due_Time, String date, String description,String userid,String class_name,String url) {
        this.name = name;
        this.due_Time = due_Time;
        this.date = date;
        this.description = description;
        this.userid=userid;
        this.class_name=class_name;
        this.url=url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDue_Time() {
        return due_Time;
    }

    public void setDue_Time(String due_Time) {
        this.due_Time = due_Time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
