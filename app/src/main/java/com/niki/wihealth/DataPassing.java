package com.niki.wihealth;

import com.niki.wihealth.model.SportCenter;

public class DataPassing {
    private static DataPassing instance;
    private String membership;
    private String location;
    private double userLat;
    private double userLon;
    private double rating;
    private SportCenter sportCenter;

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public SportCenter getSportCenter() {
        return sportCenter;
    }

    public void setSportCenter(SportCenter sportCenter) {
        this.sportCenter = sportCenter;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLon() {
        return userLon;
    }

    public void setUserLon(double userLon) {
        this.userLon = userLon;
    }

    public static DataPassing getInstance(){
        if (instance == null){
            instance = new DataPassing();
        }
        return instance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
