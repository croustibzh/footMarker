package com.georgebrown.comp2074.footmarker;

import java.io.Serializable;

public class Routes implements Serializable {
    private String imgUrl;
    private String name;
    private String hashTag;
    private long time;
    private double distance;
    private float rating;

    public Routes(String imgUrl, String name, String hashTag, long time, double distance, float rating) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.hashTag = hashTag;
        this.time = time;
        this.distance = distance;
        this.rating = rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTagTag(String tag) {
        this.hashTag = tag;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
