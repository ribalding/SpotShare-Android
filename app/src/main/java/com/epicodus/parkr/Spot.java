package com.epicodus.parkr;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Guest on 7/12/16.
 */
public class Spot {
    private String ownerID;
    private String renterID;
    private boolean isCurrentlyRented;
    private String description;
    private LatLng latLng;

    public Spot(String ownerID, String description, LatLng latLng){
        this.ownerID = ownerID;
        this.description = description;
        this.latLng = latLng;
    }

    public String getOwnerID(){
        return this.ownerID;
    }

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
        return getLatLng();
    }

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
}
