package com.example.acer.project;

/**
 * Created by Acer on 2/10/2018.
 */

public class Users {

    private String user_name;
    private String user_status;

    public Users()
    {

    }
    public Users(String user_name,String user_status)
    {
        this.user_name=user_name;
        this.user_status=user_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
}

