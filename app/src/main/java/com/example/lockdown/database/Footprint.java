package com.example.lockdown.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class Footprint {

    @PrimaryKey
    @NonNull
    public long id;

    public String address;

    public String title;

    public String time;

    public String user;

    public String music;

    public String picture;

    public String video;

}
