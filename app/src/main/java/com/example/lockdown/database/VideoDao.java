package com.example.lockdown.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface VideoDao {

    @Query("select * from Video")
    List<Video> loadAll();

    @Query("select path from Video where id = :id")
    String getPath(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideo(Video video);

    @Delete
    void deleteVideo(Video video);
}