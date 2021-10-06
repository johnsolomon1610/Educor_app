package com.example.educor_app.Classroom;

public class classroom_data {

    private String Name;
    private String Key;
    private String Create_Name;

    public classroom_data() {
    }

    public classroom_data(String name, String key,String create_Name) {
        Name = name;
        Key = key;
        Create_Name=create_Name;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
    public String getCreate_Name() {
        return Create_Name;
    }

    public void setCreate_Name(String create_Name) {
        Create_Name = create_Name;
    }
}