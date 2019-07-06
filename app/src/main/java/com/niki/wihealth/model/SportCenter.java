package com.niki.wihealth.model;

import android.support.annotation.NonNull;

import com.google.firebase.Timestamp;

public class SportCenter implements Comparable<SportCenter> {
    private String name;
    private String lon;
    private String lat;
    private String description;
    private String category;
    private long basketball;
    private long count;
    private long rating;
    private long price;
    private double distance;
    private Timestamp registration_date;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getBasketball() {
        return basketball;
    }

    public void setBasketball(long basketball) {
        this.basketball = basketball;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public Timestamp getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Timestamp registration_date) {
        this.registration_date = registration_date;
    }


    @Override
    public int compareTo(@NonNull SportCenter sportCenter) {
        if (this.distance > sportCenter.getDistance()){
            return 1;
        }
        if (this.distance < sportCenter.getDistance()){
            return -1;
        }
        return 0;
    }
}
