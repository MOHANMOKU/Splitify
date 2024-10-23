package com.trip.picnic.friends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;


public class GiveDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GiversDB";
    public static final String TABLE_NAME = "Givers";
    public static final String KEY_ID = "id";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_NAME = "name";
    public static final String KEY_PHNUM = "phNum";
    public static final String KEY_DATE = "dueDate";
    public static final String KEY_AMOUNT = "amount";

    public GiveDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLE_NAME+"("
                +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +KEY_IMAGE+" BLOB, "
                +KEY_NAME+" TEXT, "
                +KEY_PHNUM+" TEXT, "
                +KEY_DATE+" TEXT, "
                +KEY_AMOUNT+" TEXT"
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }

    public void addGiver(String nameOfGiver, String amount, String dueDate, String phNum, byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, nameOfGiver);
        contentValues.put(KEY_AMOUNT, amount);
        contentValues.put(KEY_DATE, dueDate);
        contentValues.put(KEY_PHNUM, phNum);
        contentValues.put(KEY_IMAGE, img);

        db.insert(TABLE_NAME, null, contentValues);

        db.close();
    }

    public ArrayList<GiveDetails> getGivers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        ArrayList<GiveDetails> arrayList = new ArrayList<>();

        while (cursor.moveToNext()){
            GiveDetails giveDetails = new GiveDetails();
            giveDetails.id = cursor.getInt(0);
            giveDetails.nameOfGiver = cursor.getString(2);
            giveDetails.amount = cursor.getString(5);
            giveDetails.dueDate = cursor.getString(4);
            giveDetails.phNum = cursor.getString(3);
            giveDetails.img = cursor.getBlob(1);

            arrayList.add(giveDetails);
        }
        return arrayList;
    }

    public void updateGiver(GiveDetails giveDetails){
        SQLiteDatabase db = this.getWritableDatabase();

        int id = giveDetails.id;

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, giveDetails.nameOfGiver);
        contentValues.put(KEY_AMOUNT, giveDetails.amount);
        contentValues.put(KEY_DATE, giveDetails.dueDate);
        contentValues.put(KEY_PHNUM, giveDetails.phNum);
        contentValues.put(KEY_IMAGE, giveDetails.img);

        db.update(TABLE_NAME, contentValues, KEY_ID+" = "+id, null);

        db.close();
    }

    public void deleteGiver(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID+" = ?", new String[]{String.valueOf(id)});

        db.close();
    }
}
