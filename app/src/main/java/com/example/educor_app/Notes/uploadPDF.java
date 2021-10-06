package com.example.educor_app.Notes;

public class uploadPDF {

    public String name;
    public String url;
    public String user_name;
    private String date;

    public uploadPDF() {
    }

    public uploadPDF(String name, String url,String user_name,String date) {
        this.name = name;
        this.url = url;
        this.user_name=user_name;
        this.date=date;
    }

    public String getName() {
        return name;
    }

    public String getUser_name() {
        return user_name;
    }

    public  String getUrl() {
        return  url;
    }

    public String getDate() {
        return date;
    }
}
