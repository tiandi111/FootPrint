package com.example.lockdown.database;

import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

public class DataInitializer {
    private static final int DELAY_MILLIS = 500;

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }
    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    public static void init(AppDatabase db){
        Footprint fp = new Footprint();
        fp.id = 0;
        db.fpModel().insertFootprint(fp);
    }
    public static void addFootprint(final AppDatabase db, String title){
        Footprint fp = new Footprint();
        fp.title = title;
        fp.id = db.fpModel().getMaxId() + 1;
        db.fpModel().insertFootprint(fp);
    }
    public static void addFootprint(final AppDatabase db, long id, Location address, String title,
                                    String time, String user, Music music, Pic picture, Video video){
        Footprint fp = new Footprint();
        fp.id = id;
        fp.address = address.toString();
        fp.title = title;
        fp.time = time;
        fp.user = user;
        fp.music = music.uri;
        fp.picture = picture.path;
        fp.video = video.path;
        db.fpModel().insertFootprint(fp);
    }

    public static void addMusic(final AppDatabase db, int id, String uri){
        Music music = new Music();
        music.id = id;
        music.uri = uri;
        db.musicModel().insertMusic(music);
    }

    public static void addPicture(final AppDatabase db, int id, String path){
        Pic picture = new Pic();
        picture.id = id;
        picture.path = path;
        db.picModel().insertPic(picture);
    }

    public static void addVideo(final AppDatabase db, int id, String path){
        Video video = new Video();
        video.id = id;
        video.path = path;
        db.videoModel().insertVideo(video);
    }
    private static void populateWithTestData(AppDatabase db) {

    }
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
