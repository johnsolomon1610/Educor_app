package com.example.educor_app.Assignment.Assignment_Submission;

public class upload {

    public String url,student_Username,pdf_NAME;

    public upload() {
    }

    public upload( String url,String name,String pdf_NAME) {
        this.student_Username=name;
        this.url = url;
        this.pdf_NAME=pdf_NAME;
    }

    public String getPdf_NAME() {
        return pdf_NAME;
    }

    public void setPdf_NAME(String pdf_NAME) {
        this.pdf_NAME = pdf_NAME;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public String getStudent_Username() {
        return student_Username;
    }

    public void setStudent_Username(String student_Username) {
        this.student_Username = student_Username;
    }
}
