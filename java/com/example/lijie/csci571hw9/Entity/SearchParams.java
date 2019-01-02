package com.example.lijie.csci571hw9.Entity;

import java.io.Serializable;

public class SearchParams implements Serializable {

    private static final long serialVersionUID = 73L;

    private String keyword;
    private String category;
    private int distance;
    private String unit;
    private double lat;
    private double lon;
    private String locationName;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String location) {
        this.locationName = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
