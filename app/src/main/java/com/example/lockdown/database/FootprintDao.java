package com.example.lockdown.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FootprintDao {

    @Query("select id, title from Footprint")
    List<Footprint> loadAllIdAndTitle();

    @Query("select * from Footprint where id = :id")
    Footprint loadFpById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFootprint(Footprint footprint);

    @Delete
    void deleteFootprint(Footprint footprint);

    @Query("update Footprint set title = :title where id = :id")
    void updateTitleById(String title, int id);

}
