package com.example.educor_app.Events;

public class Event_rv{
    private String User;
    private  String Time;
    private String Post;

    public Event_rv() {
    }
    public Event_rv(String User, String Time,String Post) {
        this.User = User;
        this.Time = Time;
        this.Post=Post;
    }
    public String getUser() {
        return User;
    }
    public void setUser(String User) {
        this.User = User;
    }
    public String getTime() {
        return Time;
    }
    public void setTime(String Time) {
        this.Time = Time;
    }
    public String getPost() {
        return Post;
    }
    public void setPost(String Post) {
        this.Post = Post;
    }
}