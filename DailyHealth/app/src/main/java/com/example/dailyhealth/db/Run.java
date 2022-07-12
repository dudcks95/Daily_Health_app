package com.example.dailyhealth.db;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "running")
public class Run {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public Bitmap img = null;
    public Long timestamp = 0L;
    public int distanceInMeters = 0;

    public Run(Bitmap img, Long timestamp, int distanceInMeters) {
        this.img = img;
        this.timestamp = timestamp;
        this.distanceInMeters = distanceInMeters;
    }
}
