package com.example.lijie.csci571hw9.Entity;

import java.io.Serializable;

public class Event implements Serializable {
    private static final long serialVersionUID = 37L;

    private String id;
    private String name;
    private String segment;
    private String venueName;
    private String date;
    private boolean favor;

    public Event() {

    }

    public Event(Event event) {
        this.id = event.id;
        this.name = event.name;
        this.segment = event.segment;
        this.venueName = event.venueName;
        this.date = event.date;
        this.favor = event.favor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCategory(String category) {
        this.segment = category;
    }

    public String getCategory() {
        return segment;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setFavor(boolean favor) {
        this.favor = favor;
    }

    public boolean isFavor() {
        return favor;
    }
}
