package com.epicodus.parkr;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Guest on 7/12/16.
 */
public class Spot {
    private String ownerID;
    private String renterID;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private boolean isCurrentlyRented;
    private String description;
    private LatLng latLng;

    public Spot(String ownerID, String description, LatLng latLng, String startDate, String startTime, String endDate, String endTime){
        this.ownerID = ownerID;
        this.description = description;
        this.latLng = latLng;
        this.isCurrentlyRented = false;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }


    //Getters

    public String getOwnerID(){ return this.ownerID; }

    public String getStartDate() { return this.startDate; }

    public String getStartTime() { return this.startTime; }

    public String getEndDate() { return this.endDate; }

    public String getEndTime() { return this.endTime; }

    public String getRenterID(){
        return this.renterID;
    }

    public boolean getIsCurrentlyRented(){
        return this.isCurrentlyRented;
    }

    public String getDescription(){
        return this.description;
    }

    public LatLng getLatLng(){
        return this.latLng;
    }


    //Setters

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setRenterID(String renterID) {
        this.renterID = renterID;
    }

    public void setCurrentlyRented(boolean currentlyRented) {
        isCurrentlyRented = currentlyRented;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public void setStartTime(String startTime) { this.startTime = startTime; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public void setEndTime(String endTime) { this.endTime = endTime; }


}
