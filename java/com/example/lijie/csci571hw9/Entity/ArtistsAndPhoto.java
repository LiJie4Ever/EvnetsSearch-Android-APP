package com.example.lijie.csci571hw9.Entity;

import java.util.ArrayList;

public class ArtistsAndPhoto {

    private ArrayList<String> photosList;
    private String name;
    private String followersNum;
    private String popularityNum;
    private String url;

    public ArtistsAndPhoto() {
        photosList = new ArrayList<>();
    }

    public ArtistsAndPhoto(ArtistsAndPhoto artistsAndPhoto) {
        this.name = artistsAndPhoto.name;
        this.followersNum = artistsAndPhoto.followersNum;
        this.popularityNum = artistsAndPhoto.popularityNum;
        this.url = artistsAndPhoto.url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFollowersNum() {
        return followersNum;
    }

    public void setFollowersNum(String followersNum) {
        this.followersNum = followersNum;
    }

    public String getPopularityNum() {
        return popularityNum;
    }

    public void setPopularityNum(String popularityNum) {
        this.popularityNum = popularityNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getPhotosList() {
        return photosList;
    }
}
