package com.epicodus.parkr.models;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

/**
 * Created by Guest on 7/12/16.
 */

@Parcel
public class Spot {
    String spotID;
    String ownerID;
    String renterID;
    String startDate;
    String startTime;
    String endDate;
    String endTime;
    boolean isCurrentlyRented;
    String description;
    LatLng latLng;
    String address;
    double lat;
    double lng;

    public Spot() {}

    public Spot(String spotID, String ownerID, String address, String description, Double lat, Double lng, String startDate, String startTime, String endDate, String endTime, boolean isCurrentlyRented) {
        this.spotID = spotID;
        this.ownerID = ownerID;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.isCurrentlyRented = isCurrentlyRented;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.address = address;
    }


    //Getters


    public String getSpotID() {
        return spotID;
    }

    public String getOwnerID() {
        return this.ownerID;
    }

    public String getAddress() {
        return this.address;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getRenterID() {
        return this.renterID;
    }

    public boolean isCurrentlyRented() {
        return this.isCurrentlyRented;
    }

    public String getDescription() {
        return this.description;
    }

    public LatLng getLatLng() {
        return this.latLng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    //Setters


    public void setSpotID(String spotID) {
        this.spotID = spotID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setLatLng(double lat, double lng) {
        this.latLng = new LatLng(lat, lng);
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}