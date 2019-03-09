package com.example.lockdown.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MusicDao {

    @Query("select * from Music")
    List<Music> loadAll();

    @Query("select uri from Music where id = :id")
    String getUri(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMusic(Music music);

    @Delete
    void deleteMusic(Music music);
}

