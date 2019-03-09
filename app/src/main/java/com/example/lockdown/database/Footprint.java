package com.example.lockdown.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.location.Location;


@Entity
public class Footprint {

    @PrimaryKey
    @NonNull
    public String id;

    public Location address;

    public String title;

    public String time;

    public String user;

    public Music music;

    public Pic picture;

    public Video video;

}
