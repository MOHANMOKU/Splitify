package com.trip.picnic.friends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProfileDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ProfileDB";
    public static final String TABLE_NAME = "Profile";
    public static final String KEY_ID = "id";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHNUM = "phNum";


    public ProfileDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME+"("
                                +KEY_ID+" INTEGER PRIMARY KEY , "
                                +KEY_IMAGE+" BLOB, "
                                +KEY_NAME+" TEXT, "
                                +KEY_PHNUM+" TEXT"
                +")");

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, "1");
        contentValues.put(KEY_IMAGE, (String) null);
        contentValues.put(KEY_NAME, "Name");
        contentValues.put(KEY_PHNUM, "PhNum");

        db.insert(TABLE_NAME,null, contentValues);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);

    }

    public ProfileModel getDetails(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        while(cursor.moveToNext()  && cursor.getInt(0)==1) {
            ProfileModel model = new ProfileModel();
            model.id = cursor.getInt(0);
            model.img = cursor.getBlob(1);
            model.name = cursor.getString(2);
            model.phNum = cursor.getString(3);
            db.close();
            return model;
        }
        return null;
    }

    public void updateDetail(ProfileModel profileModel, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_IMAGE, profileModel.img);
        contentValues.put(KEY_NAME, profileModel.name);
        contentValues.put(KEY_PHNUM, profileModel.phNum);

        try {
            int rowsUpdated = db.update(TABLE_NAME, contentValues, KEY_ID + " = 1", null);
            Toast.makeText(context, "UPDATED", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Change the Image", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}
