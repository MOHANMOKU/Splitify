package com.trip.picnic.friends;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GiveDetails {

    int id;
    String nameOfGiver;
    String amount;
    String dueDate;
    String phNum;
    byte[] img;

    public GiveDetails() {
    }


    public GiveDetails(int id,String nameOfGiver, String amount, String dueDate, String phNum , byte[] image) {
        this.id = id;
        this.nameOfGiver = nameOfGiver;
        this.amount = amount;
        this.dueDate = dueDate;
        this.phNum = phNum;
        this.img = image;
    }

    public GiveDetails(String nameOfGiver, String amount, String dueDate, String phNum, byte[] img) {
        this.nameOfGiver = nameOfGiver;
        this.amount = amount;
        this.dueDate = dueDate;
        this.phNum = phNum;
        this.img = img;
    }

    public Bitmap convertByteToBit(byte[] img){
        Bitmap image = BitmapFactory.decodeByteArray(img, 0, img.length);
        return image;
    }

}
