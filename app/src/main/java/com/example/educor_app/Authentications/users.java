package com.example.educor_app.Authentications;

public class users {
    public String Username,email,MobileNumber,qualification,uid;
    public  users(){
    }
    public users(String Name, String Email, String Phone_number,String Qualification,String userid) {
        this.Username= Name;
        this.email=Email;
        this.MobileNumber = Phone_number;
        this.qualification=Qualification;
        this.uid=userid;
    }
}
