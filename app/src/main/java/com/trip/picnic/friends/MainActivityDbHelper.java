package com.trip.picnic.friends;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = MainActivityModel.class, exportSchema = false, version = 1)
public abstract class MainActivityDbHelper extends RoomDatabase {

    public abstract MainActivityDoa mainActivityDoa();


}
