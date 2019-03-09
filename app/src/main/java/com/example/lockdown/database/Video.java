package com.example.lockdown.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Video {

    @PrimaryKey
    @NonNull
    public int id;

    public String path;

}