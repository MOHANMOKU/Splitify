package com.trip.picnic.friends;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainDayActivity extends AppCompatActivity {

    private LinearLayout container,container2;

    private int userInputValue;
    Map<String, Integer> names = new HashMap<>();
    Map<String, Integer> placePaidBy = new HashMap<>();
    ArrayList<String> places = new ArrayList<>();
    ArrayList<String> peopleNames = new ArrayList<>();

    EditText[] name;
    TextView[] cost;
    CardView[] putColor;

    Button nameComplete, peopleButton;
    ImageButton addButton, completeActivity;
    EditText editText;

    private int flag;
    TempDBHelper tempDBHelper;

    MainActivityDbHelper mainActivityDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_day);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        peopleButton = findViewById(R.id.peopleButton);
        addButton = findViewById(R.id.add_Button3);
        editText = findViewById(R.id.numberOfPeople);
        completeActivity = findViewById(R.id.completeButton);
        nameComplete = findViewById(R.id.nameComplete);
        container = findViewById(R.id.container);

        completeActivity.setEnabled(false);
        addButton.setEnabled(false);
        nameComplete.setEnabled(false);

        tempDBHelper = TempDBHelper.getDB(this);
        ArrayList<Temp> tempArray= (ArrayList<Temp>) tempDBHelper.tempDao().getAllTempData();

        if(tempArray.size() != 0){

            userInputValue = tempArray.get(0).getNumber();
            names = tempArray.get(0).getExpenses();
            placePaidBy = tempArray.get(0).getPaid();
            places = tempArray.get(0).getPlaces();
            peopleNames = tempArray.get(0).getNames();

            flag = 1;

            name = new EditText[userInputValue];
            cost = new TextView[userInputValue];
            putColor = new CardView[userInputValue];

            peopleButton.setEnabled(false);
            addButton.setEnabled(true);
            completeActivity.setEnabled(true);

            for (int i = 0; i < userInputValue; i++) {
                View cardView = LayoutInflater.from(MainDayActivity.this).inflate(R.layout.single_in_day_activity, null);
                cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 260));
                name[i] = cardView.findViewById(R.id.name);
                putColor[i] = cardView.findViewById(R.id.indicationColour);
                cost[i] = cardView.findViewById(R.id.amount5);
                container.addView(cardView);
            }

            for (int i = 0; i < userInputValue; i++) {
                String tempName = peopleNames.get(i);
                int giveOrTake = placePaidBy.get(tempName) - names.get(tempName);
                name[i].setText(tempName);
                cost[i].setText("" + giveOrTake);
                if (giveOrTake > 0) {
                    putColor[i].setBackgroundColor(Color.rgb(0, 255, 0));
                } else if (giveOrTake < 0) {
                    putColor[i].setBackgroundColor(Color.rgb(255, 0, 0));
                } else {
                    putColor[i].setBackgroundColor(Color.rgb(0,0,0));
                }
                name[i].setInputType(InputType.TYPE_NULL);
                name[i].setKeyListener(null);
                name[i].setCursorVisible(false);
                name[i].setFocusable(false);
                name[i].setClickable(false);
            }

        }

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                try{
                if(!(editText.getText().toString().equals(""))) {

                    peopleButton.setClickable(false);
                    nameComplete.setEnabled(true);
                    userInputValue =Integer.parseInt(editText.getText().toString());
                    name = new EditText[userInputValue];
                    cost = new TextView[userInputValue];
                    putColor = new CardView[userInputValue];

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    for (int i = 0; i < userInputValue; i++) {
                        View cardView = LayoutInflater.from(MainDayActivity.this).inflate(R.layout.single_in_day_activity, null);
                        cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 260));
                        name[i] = cardView.findViewById(R.id.name);
                        putColor[i] = cardView.findViewById(R.id.indicationColour);
                        cost[i] = cardView.findViewById(R.id.amount5);
                        container.addView(cardView);
                    }

                } else {
                    Toast.makeText(MainDayActivity.this, "Enter the value",Toast.LENGTH_SHORT).show();

                }
                } catch (Exception e){
                    Toast.makeText(MainDayActivity.this, "DATA ADDED", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nameComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                if(checkArraySize(name, userInputValue)){
                    nameComplete.setEnabled(false);
                    addButton.setEnabled(true);
                    for(int j = 0; j < userInputValue ;j++){
                        if(peopleNames.size() < userInputValue) {
                            //j=peopleNames.size();
                            peopleNames.add(name[j].getText().toString());
                            names.put(name[j].getText().toString(), 0);
                            placePaidBy.put(name[j].getText().toString(), 0);
                            name[j].setInputType(InputType.TYPE_NULL);
                            name[j].setKeyListener(null);
                            name[j].setCursorVisible(false);
                            name[j].setFocusable(false);
                            name[j].setClickable(false);
                        }
                    }
                    Toast.makeText(MainDayActivity.this, "Data Entered", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainDayActivity.this, "fill All Rows", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeActivity.setEnabled(true);
                Dialog dialog = new Dialog(MainDayActivity.this);
                dialog.setContentView(R.layout.day_activity_dailog);

                container2 = dialog.findViewById(R.id.nameListShower);
                EditText placeName = dialog.findViewById(R.id.placeName);
                TextView[] namesToDisplay = new TextView[userInputValue];
                EditText[] costOfPerson = new EditText[userInputValue];
                Button completeButton = dialog.findViewById(R.id.addDetails);

                Spinner spinner = dialog.findViewById(R.id.spinner);
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MainDayActivity.this, android.R.layout.simple_spinner_dropdown_item, peopleNames);
                spinner.setAdapter(spinnerAdapter);
                for (int i = 0; i < userInputValue; i++){
                    View listview = LayoutInflater.from(MainDayActivity.this).inflate(R.layout.ashwath,null);
                    namesToDisplay[i] = listview.findViewById(R.id.nameOfPerson);
                    namesToDisplay[i].setText(peopleNames.get(i));
                    costOfPerson[i] = listview.findViewById(R.id.amountOfPerson);
                    container2.addView(listview);
                }

                completeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(checkArraySize(costOfPerson, userInputValue) && !(placeName.getText().toString().equals(""))) {


                           String paidBy = (String) spinner.getSelectedItem();
                           int totalExpense = 0;
                           for (int i = 0; i < userInputValue; i++) {
                               totalExpense = totalExpense + Integer.parseInt(costOfPerson[i].getText().toString());
                               int costPerPerson = (Integer.parseInt(costOfPerson[i].getText().toString())) + names.get(namesToDisplay[i].getText().toString());
                               names.put(namesToDisplay[i].getText().toString(), costPerPerson);
                           }

                           places.add(placeName.getText().toString()+" : "+totalExpense+" : "+paidBy);
                           Toast.makeText(MainDayActivity.this, "Cost : " + totalExpense, Toast.LENGTH_SHORT).show();

                           totalExpense += placePaidBy.get(paidBy);
                           placePaidBy.put(paidBy, totalExpense);

                           for (int i = 0; i < userInputValue; i++) {
                               String tempName = peopleNames.get(i);
                               int giveOrTake = placePaidBy.get(tempName) - names.get(tempName);
                               cost[i].setText("" + giveOrTake);
                               if (giveOrTake > 0) {
                                   putColor[i].setBackgroundColor(Color.rgb(0, 255, 0));
                               } else if (giveOrTake < 0) {
                                   putColor[i].setBackgroundColor(Color.rgb(255, 0, 0));
                               } else {
                                   putColor[i].setBackgroundColor(Color.rgb(0,0,0));
                               }
                           }

                           if(flag != 1){

                               flag = 1;

                               Date today = new Date();
                               SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
                               String formattedDate = formatter.format(today);

                               tempDBHelper.tempDao().addTempData(
                                    new Temp(1, userInputValue, names,placePaidBy, peopleNames, places, formattedDate)
                               );

                           } else {

                               Date today = new Date();
                               SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
                               String formattedDate = formatter.format(today);

                               tempDBHelper.tempDao().updateTemp(
                                     new Temp(1, userInputValue, names,placePaidBy, peopleNames, places, formattedDate)
                               );

                           }
                           dialog.dismiss();
                       }else {
                           Toast.makeText(MainDayActivity.this, "Exited", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
                dialog.show();
            }
        });

        completeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date today = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
                String formattedDate = formatter.format(today);

                new Thread(() -> {
                    MainActivityInstance instance = MainActivityInstance.getInstance(MainDayActivity.this);
                    MainActivityDbHelper dbHelper = instance.getDatabase();
                    MainActivityDoa mainActivityDoa = dbHelper.mainActivityDoa();
                    mainActivityDoa.addActivityData(new MainActivityModel(userInputValue, peopleNames, places, names, placePaidBy, formattedDate));
                }).start();
                tempDBHelper.tempDao().deleteTemp(
                        new Temp(1, userInputValue, names,placePaidBy, peopleNames, places, formattedDate)
               );
                finish();
            }
        });
    }

    static boolean checkArraySize(EditText[] name , int size){
        for(int i=0;i<size;i++){
            if(name[i].getText().toString().equals("")){
                return false;
            }
        }
        return true;
    }

}