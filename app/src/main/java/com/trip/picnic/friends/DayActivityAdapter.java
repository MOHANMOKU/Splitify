package com.trip.picnic.friends;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class DayActivityAdapter extends RecyclerView.Adapter<DayActivityAdapter.ViewHolder> {

    Context context;
    ArrayList<MainActivityModel> arrayActivity;
    DayActivityAdapter(Context context, ArrayList<MainActivityModel> arrayActivity) {
        this.context = context;
        this.arrayActivity = arrayActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View v = LayoutInflater.from(context).inflate(R.layout.day_activity, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.txtdate.setText(arrayActivity.get(position).date);
        holder.txtnumOfMembers.setText(String.valueOf(arrayActivity.get(position).number));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to delete this Activity")
                        .setIcon(R.drawable.delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new Thread(() -> {
                                    MainActivityInstance instance = MainActivityInstance.getInstance(context);
                                    MainActivityDbHelper dbHelper = instance.getDatabase();
                                    MainActivityDoa mainActivityDoa = dbHelper.mainActivityDoa();
                                    mainActivityDoa.deleteActivity(new MainActivityModel(
                                            arrayActivity.get(position).getId(),
                                            arrayActivity.get(position).getNumber(),
                                            arrayActivity.get(position).getNames(),
                                            arrayActivity.get(position).getPlaces(),
                                            arrayActivity.get(position).getExpenses(),
                                            arrayActivity.get(position).getPaid(),
                                            arrayActivity.get(position).getDate()
                                    ));
                                }).start();

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                try{
                                    
                               
                                    arrayActivity.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                    notifyDataSetChanged();
                                } catch (Exception e){
                                    Toast.makeText(context, "Database Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.display_day_activity);
                LinearLayout container = dialog.findViewById(R.id.container1);
                LinearLayout container2 = dialog.findViewById(R.id.toDisplayPlaces);
                int personNum = arrayActivity.get(position).getNumber();
                ArrayList<String> peopleNames = arrayActivity.get(position).getNames();
                ArrayList<String> places = arrayActivity.get(position).getPlaces();
                Map<String, Integer> names = arrayActivity.get(position).getExpenses();
                Map<String, Integer> placePaidBy = arrayActivity.get(position).getPaid();
                EditText[] name = new EditText[personNum];
                TextView[] cost = new TextView[personNum];
                CardView[] putColor = new CardView[personNum];
                TextView number = dialog.findViewById(R.id.numberOfPeople1);
                number.setText(""+personNum);

                for (int i = 0; i < personNum; i++) {
                    View cardView = LayoutInflater.from(context).inflate(R.layout.singlepersonactivityfordetails, null);
                    cardView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 260));
                    name[i] = cardView.findViewById(R.id.name1);
                    putColor[i] = cardView.findViewById(R.id.indicationColour1);
                    cost[i] = cardView.findViewById(R.id.amount51);
                    container.addView(cardView);
                }

                for (int i = 0; i < personNum; i++) {
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

                for (int i = 0; i < places.size(); i++) {
                    View cardView = LayoutInflater.from(context).inflate(R.layout.text_view_layout, null);
                    TextView placeText = cardView.findViewById(R.id.displayPlaces12);
                    placeText.setText(places.get(i));
                    container2.addView(cardView);
                }

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayActivity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtdate, txtnumOfMembers;
        ImageButton details;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            txtdate = itemView.findViewById(R.id.daydate);
            txtnumOfMembers = itemView.findViewById(R.id.daymembers);
            details= itemView.findViewById(R.id.details);
            deleteButton = itemView.findViewById(R.id.delete);
        }
    }

}
