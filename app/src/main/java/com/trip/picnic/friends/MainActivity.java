package com.trip.picnic.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    ArrayList<MainActivityModel> dayActivityArray = new ArrayList<MainActivityModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Thread(() -> {
            MainActivityInstance instance = MainActivityInstance.getInstance(MainActivity.this);
            MainActivityDbHelper dbHelper = instance.getDatabase();
            MainActivityDoa mainActivityDoa = dbHelper.mainActivityDoa();
            dayActivityArray = (ArrayList<MainActivityModel>) mainActivityDoa.getAllActivityData();
        }).start();

        RecyclerView recyclerView = findViewById(R.id.recyclerday);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        DayActivityAdapter dayActivityAdapter =new DayActivityAdapter(this, dayActivityArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dayActivityAdapter);

        Intent needToGiveIntent = new Intent(MainActivity.this, NeedToGive.class);
        ImageButton needToGiveButton = findViewById(R.id.need_to_give);
        needToGiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(needToGiveIntent);

            }
        });

        Intent needToTakeIntent = new Intent(MainActivity.this, NeedToTake.class);
        ImageButton needToTakeButton = findViewById(R.id.need_to_take);
        needToTakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(needToTakeIntent);
            }
        });

        ImageButton profileButton = findViewById(R.id.profile);
        Intent profile = new Intent(MainActivity.this, Profile.class);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(profile);
            }
        });

        ImageButton addButton = findViewById(R.id.add_Button);
        Intent newActivity = new Intent(MainActivity.this, MainDayActivity.class);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(newActivity);
            }
        });

        ImageButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
                Toast.makeText(MainActivity.this, "Refersh Completed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}