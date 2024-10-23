package com.trip.picnic.friends;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NeedToGive extends AppCompatActivity {

    private ArrayList<GiveDetails> giveArray = new ArrayList<GiveDetails>();
    private GiveDetailsAdapter adapter;
    private ImageView imageView;
    private String format = "dd/MM/yyyy";
    private static final int REQUEST_IMAGE_SELECT = 1;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_need_to_give);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GiveDBHelper giveDBHelper = new GiveDBHelper(NeedToGive.this);
        RecyclerView recyclerView =findViewById(R.id.recyclerInGive);
        ImageButton addButton = findViewById(R.id.addButtonInGive);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(NeedToGive.this);
                dialog.setContentView(R.layout.add_need_to_give_n_take);

                imageView = dialog.findViewById(R.id.imageView);
                Button addButton = dialog.findViewById(R.id.add_Button1);

                EditText edtName = dialog.findViewById(R.id.name);
                EditText edtPhNum = dialog.findViewById(R.id.phNum);
                EditText edtDate = dialog.findViewById(R.id.dayDate2);
                EditText amount = dialog.findViewById(R.id.amount);
                ImageView image = dialog.findViewById(R.id.imageView);

                Button action = dialog.findViewById(R.id.addAction);

                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = edtName.getText().toString();
                        String phnum= edtPhNum.getText().toString();
                        String date = edtDate.getText().toString();
                        String amount1 = amount.getText().toString();
                        try{
                            Bitmap bitmap;

                            if(flag == 1){
                                bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                            }else{
                                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.userprofile1);
                            }

                        if(!(name.equals("") || phnum.equals("") || date.equals("") || amount1.equals(""))) {

                            if(isValidDate(date, format)){

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                byte[] imageBytes = bos.toByteArray();

                                giveDBHelper.addGiver(name, amount1, date, phnum, imageBytes);

                                giveArray.add(new GiveDetails(name,amount1, date, phnum,imageBytes));
                                adapter.notifyItemInserted(giveArray.size()-1);
                                recyclerView.scrollToPosition(giveArray.size()-1);
                                dialog.dismiss();
                                recreate();
                            } else {
                                Toast.makeText(NeedToGive.this, "Enter correct Date",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(NeedToGive.this, "Don't leave Empty1",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){
                            Toast.makeText(NeedToGive.this, "Add Image", Toast.LENGTH_SHORT).show();
                    }
                    }
                });

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        ((Activity) NeedToGive.this).startActivityForResult(intent, REQUEST_IMAGE_SELECT);
                    }
                });

                dialog.show();
            }
        });

        giveArray = giveDBHelper.getGivers();

        adapter = new GiveDetailsAdapter(this, giveArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data!= null) {
            try {
                Uri selectedImage = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                bitmap = resizeBitmap(bitmap);
                flag = 1;
                imageView.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this, "Try Different Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isValidDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

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