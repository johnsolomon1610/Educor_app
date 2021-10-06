package com.example.educor_app.Authentications;

public class signup {

    public String Username,Email,MobileNumber,institution,qualification,Userid;
    public  signup(){
    }
    public signup(String Name, String Email, String Phone_number,String Institution ,String Qualification,String userid) {
        this.Username= Name;
        this.Email=Email;
        this.institution=Institution;
        this.MobileNumber = Phone_number;
        this.qualification=Qualification;
        this.Userid=userid;
    }
}
