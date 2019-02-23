package com.example.lockdown.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PicDao {

    @Query("select * from Pic")
    List<Pic> loadAll();

    @Query("select path from Pic where id = :id")
    String getPath(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPic(Pic picture);

    @Delete
    void deletePic(Pic picture);
}

