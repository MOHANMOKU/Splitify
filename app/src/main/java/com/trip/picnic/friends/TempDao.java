package com.trip.picnic.friends;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TempDao {

    @Query("SELECT * FROM Temp")
    List<Temp> getAllTempData();

    @Insert
    void addTempData(Temp temp);

    @Update
    void updateTemp(Temp temp);

    @Delete
    void deleteTemp(Temp temp);

}
