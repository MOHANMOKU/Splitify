package com.trip.picnic.friends;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MainActivityDoa {

    @Query("SELECT * FROM MainActivityModel")
    List<MainActivityModel> getAllActivityData();

    @Insert
    void addActivityData(MainActivityModel mainActivityModel);

    @Update
    void updateActivity(MainActivityModel mainActivityModel);

    @Delete
    void deleteActivity(MainActivityModel mainActivityModel);
}
