package com.epicodus.parkr;

/**
 * Created by Ryan on 7/8/2016.
 */
public class User {
    private String user_name;
    private String password;
    private String email;
    private String full_name;

    public User(){}

    public User(String name, String pass, String email, String fullName){
        this.user_name = name;
        this.password = pass;
        this.email = email;
        this.full_name = fullName;
    }

    public String getUserName(){
        return this.user_name;
    }

    public String getPassword(){
        return this.password;
    }

    public String getEmail(){
        return this.email;
    }

    public String getFullName(){
        return this.full_name;
    }

    public void setUserName(String newName){
        this.user_name = newName;
    }

    public void setPassword(String newPass){
        this.password = newPass;
    }

    public void setEmail(String newEmail){
        this.email = newEmail;
    }

    public void setFullName(String fullName){
        this.full_name = fullName;
    }
}
