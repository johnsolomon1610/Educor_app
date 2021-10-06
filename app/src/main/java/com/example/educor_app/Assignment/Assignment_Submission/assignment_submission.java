package com.example.educor_app.Assignment.Assignment_Submission;

public class assignment_submission {

    String Title,Url,Student_Username,Teacher_Userid,Student_Userid,Class_name,pdf_NAME;

    public assignment_submission(){

    }

    public assignment_submission(String title,String url,String name,String teacher_Userid,String student_userid,String class_name, String pdfNAME)
    {
        this.Title=title;
        this.Url=url;
        this.Student_Username=name;
        this.Teacher_Userid=teacher_Userid;
        this.Student_Userid=student_userid;
        this.Class_name=class_name;
        this.pdf_NAME=pdfNAME;
    }

    public String getPdf_NAME() {
        return pdf_NAME;
    }

    public void setPdf_NAME(String pdf_NAME) {
        this.pdf_NAME = pdf_NAME;
    }

    public String getClass_name() {
        return Class_name;
    }

    public void setClass_name(String class_name) {
        Class_name = class_name;
    }

    public String getStudent_Userid() {
        return Student_Userid;
    }

    public void setStudent_Userid(String student_Userid) {
        Student_Userid = student_Userid;
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

    public String getTeacher_Userid() {
        return Teacher_Userid;
    }

    public void setTeacher_Userid(String teacher_Userid) {
        Teacher_Userid = teacher_Userid;
    }
}
