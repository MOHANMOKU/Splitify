package com.trip.picnic.friends;

import android.content.Context;

import androidx.room.Room;

public class MainActivityInstance {

    public static MainActivityInstance instance;
    private MainActivityDbHelper mainActivityDbHelper;

    private MainActivityInstance(Context context) {
        mainActivityDbHelper = Room.databaseBuilder(context, MainActivityDbHelper.class, "mydatabase.db")
                .build();
    }

    public static synchronized MainActivityInstance getInstance(Context context) {
        if (instance == null) {
            instance = new MainActivityInstance(context);
        }
        return instance;
    }

    public MainActivityDbHelper getDatabase() {
        return mainActivityDbHelper;
    }

}
