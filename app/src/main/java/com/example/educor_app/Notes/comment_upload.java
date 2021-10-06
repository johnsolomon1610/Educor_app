package com.example.educor_app.Notes;

public class comment_upload {
    public String comment;
    public String user_name;
    private String date;

    public comment_upload() {
    }

    public comment_upload(String comment, String user_name, String date) {
        this.comment = comment;
        this.user_name = user_name;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getDate() {
        return date;
    }
}
