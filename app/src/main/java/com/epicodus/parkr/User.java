package com.epicodus.parkr;

import java.util.ArrayList;

/**
 * Created by Ryan on 7/8/2016.
 */
public class User {
    private String password;
    private String email;
    private String full_name;
    private String firebaseAuthID;
    private ArrayList<Spot> rentedSpots;
    private ArrayList<Spot> ownedSpots;


    public User(){}

    public User(String id, String fullName, String pass, String email){
        this.firebaseAuthID = id;
        this.password = pass;
        this.email = email;
        this.full_name = fullName;
    }



    public String getPassword(){return this.password;}

    public String getEmail(){
        return this.email;
    }

    public String getFullName(){
        return this.full_name;
    }

    public ArrayList<Spot> getRentedSpots(){ return this.rentedSpots; }

    public ArrayList<Spot> getOwnedSpots(){ return this.ownedSpots; }

    public String getFirebaseAuthID(){return this.firebaseAuthID;}


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
