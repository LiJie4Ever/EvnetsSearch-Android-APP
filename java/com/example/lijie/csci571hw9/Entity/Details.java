package com.example.lijie.csci571hw9.Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class Details implements Serializable {

    private static final long serialVersionUID = 13L;

    private String id;
    private String name;
    private String venueName;
    private String segment;
    private String time;
    private String maxPrice;
    private String minPrice;
    private String status;
    private String url;
    private String seatMapUrl;
    private ArrayList<String> artists;
    private ArrayList<String> category;
    private double lat;
    private double lon;

    public Details() {
        artists = new ArrayList<>();
        category = new ArrayList<>();
    }

    public Details(Details detail) {
        this.name = detail.name;
        this.venueName = detail.venueName;
        this.time = detail.time;
        this.maxPrice = detail.maxPrice;
        this.minPrice = detail.minPrice;
        this.status = detail.status;
        this.url = detail.url;
        this.seatMapUrl = detail.seatMapUrl;
        this.artists = detail.artists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeatMapUrl() {
        return seatMapUrl;
    }

    public void setSeatMapUrl(String seatMapUrl) {
        this.seatMapUrl = seatMapUrl;
    }

    public ArrayList<String> getArtists() {
        return artists;
    }

    public ArrayList<String> getCategory() {
        return category;
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

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }
}
