package com.example.dailyhealth.db;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "running")
public class Run {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Bitmap img = null;
    public long timestamp = 0L;
    public Long timeInMillis = 0L;
    public String title = null;
    public float distanceInMeters = 0;


    public Run( Bitmap img, long timestamp, Long timeInMillis, String title, float distanceInMeters) {
        this.img = img;
        this.timestamp = timestamp;
        this.timeInMillis = timeInMillis;
        this.title = title;
        this.distanceInMeters = distanceInMeters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(Long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(float distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }
}
