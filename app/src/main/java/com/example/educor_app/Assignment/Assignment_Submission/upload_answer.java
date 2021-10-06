package com.example.educor_app.Assignment.Assignment_Submission;

public class upload_answer {

    String Title,Url,Student_Username,Student_Userid,Teacher_userid,Class_name,PDF_NAME;

    public upload_answer() {
    }

    public upload_answer(String title, String url, String user_name, String userid,String teacher_userid,String class_name,String pdf) {
        this.Title = title;
        this.Url = url;
        this.Student_Username = user_name;
        this.Student_Userid = userid;
        this.Teacher_userid=teacher_userid;
        this.Class_name=class_name;
        this.PDF_NAME=pdf;
    }

    public String getClass_name() {
        return Class_name;
    }

    public void setClass_name(String class_name) {
        Class_name = class_name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getStudent_Username() {
        return Student_Username;
    }

    public void setStudent_Username(String student_Username) {
        Student_Username = student_Username;
    }

    public String getStudent_Userid() {
        return Student_Userid;
    }

    public void setStudent_Userid(String student_Userid) {
        Student_Userid = student_Userid;
    }

    public String getTeacher_userid() {
        return Teacher_userid;
    }

    public void setTeacher_userid(String teacher_userid) {
        Teacher_userid = teacher_userid;
    }

    public String getPDF_NAME() {
        return PDF_NAME;
    }

    public void setPDF_NAME(String PDF_NAME) {
        PDF_NAME = PDF_NAME;
    }
}
