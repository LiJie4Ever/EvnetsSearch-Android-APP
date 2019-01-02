package com.example.lijie.csci571hw9.Entity;

import java.io.Serializable;

public class UpcomingEvents implements Serializable {

    private String name;
    private String artist;
    private String date;
    private String type;
    private String url;
    private String dateForOrder;

    public UpcomingEvents() {

    }

    public UpcomingEvents(UpcomingEvents event) {
        this.name = event.name;
        this.artist = event.artist;
        this.date = event.date;
        this.type = event.type;
        this.url = event.url;
        this.dateForOrder = event.dateForOrder;
    }

    public String getDateForOrder() {
        return dateForOrder;
    }

    public void setDateForOrder(String dateForOrder) {
        this.dateForOrder = dateForOrder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
