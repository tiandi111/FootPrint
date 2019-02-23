package com.example.lockdown.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(foreignKeys = {
        @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user")
})
public class FootPrint {
    @PrimaryKey
    @NonNull
    private String id;

    private String user;

    private String name;

    private Date addedTime;

    private String music;

    private String image;

    private String video;

}
