package com.example.educor_app.Assignment.Comments;

public class comments {

    String comments,date,student_name;

    public comments() {
    }

    public comments(String comments, String date, String student_name) {
        this.comments = comments;
        this.date = date;
        this.student_name = student_name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
}
