package com.georgebrown.comp2074.footmarker;

import java.io.Serializable;
import java.sql.Blob;

public class RouteDetails implements Serializable {
    private int id;
    private String name;
    private String hashTag;
    private double distance;
    private long time;
    private float rating;
    private byte[] image;
    public RouteDetails(int id, String name, double distance, long time, float rating, byte[] image, String hashTag) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.rating = rating;
        this.image = image;
        this.hashTag = hashTag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance/1000;
    }

    public void setDistance(double distance) { this.distance = distance; }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public long getTime() { return time; }

    public void setTime(long time) {
        this.time = time;
    }

    public String getHashTag() { return hashTag; }

    public void setHashTag(String hashTag) { this.hashTag = hashTag; }


}
