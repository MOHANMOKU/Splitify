package com.trip.picnic.friends;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ProfileModel {

    int id;
    byte[] img;
    String name;
    String phNum;

    public Bitmap convertByteToBit(byte[] img){
        Bitmap image = BitmapFactory.decodeByteArray(img, 0, img.length);
        return image;
    }


}
