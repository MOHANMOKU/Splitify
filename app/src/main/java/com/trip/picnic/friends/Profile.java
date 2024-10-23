package com.trip.picnic.friends;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    ImageView profile;
    Button openGallery,update;
    EditText name,pNum;
    Bitmap bitImg,bitmap;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ProfileDBHelper dbHelper = new ProfileDBHelper(this);
        profile = findViewById(R.id.imgSet);
        openGallery = findViewById(R.id.openGallery);
        name = findViewById(R.id.txtVName);
        pNum = findViewById(R.id.txtPNum);
        update = findViewById(R.id.update);

        ProfileModel model = dbHelper.getDetails();

        if(model.img != null) {
            bitmap = model.convertByteToBit(model.img);
            profile.setImageBitmap(bitmap);
        }
        name.setText((String)model.name);
        pNum.setText((String)model.phNum);

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bitImg = ((BitmapDrawable) profile.getDrawable()).getBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitImg.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] imageBytes = bos.toByteArray();

                    ProfileModel model = new ProfileModel();
                    model.id = 1;
                    model.img = imageBytes;
                    model.name = name.getText().toString();
                    model.phNum = pNum.getText().toString();


                    dbHelper.updateDetail(model, Profile.this);
                }catch (Exception e){
                    Toast.makeText(Profile.this, "Check the Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickImage(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(gallery);
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        Uri imageUri = result.getData().getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        bitmap = resizeBitmap(bitmap);
                        //flag = 1;
                        profile.setImageBitmap(bitmap);
                    } catch (Exception e){
                        Toast.makeText(Profile.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    public Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newSize = Math.min(width, height);
        int x = (width - newSize) / 2;
        int y = (height - newSize) / 2;

        Bitmap squareBitmap = Bitmap.createBitmap(bitmap, x, y, newSize, newSize);

        float scale = 1024.0f / newSize;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(squareBitmap, 0, 0, newSize, newSize, matrix, true);

        return resizedBitmap;
    }
}

