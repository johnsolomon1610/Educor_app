package com.example.educor_app.Assignment;

public class uploadPDF {
    public String name;
    public String url;
    public String user_name;
    private String date;
    private String Due_Time,Description,Userid,class_name;

    public uploadPDF() {
    }

    public uploadPDF(String name, String url,String user_name,String date,String due_time,String description,String user_id,String class_name) {
        this.name = name;
        this.class_name=class_name;
        this.url = url;
        this.user_name=user_name;
        this.date=date;
        this.Due_Time=due_time;
        this.Description=description;
        this.Userid=user_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDue_Time() {
        return Due_Time;
    }

    public void setDue_Time(String due_Time) {
        Due_Time = due_Time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }
}
