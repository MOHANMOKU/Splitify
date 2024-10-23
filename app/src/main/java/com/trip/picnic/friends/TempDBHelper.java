package com.trip.picnic.friends;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = Temp.class, exportSchema = false, version = 2)
@TypeConverters({Type_Converters.class})
public abstract class TempDBHelper extends RoomDatabase {

    public static final String DB_NAME = "TempDB";
    public static TempDBHelper instance;

    public static synchronized TempDBHelper getDB(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, TempDBHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract TempDao tempDao();

}
