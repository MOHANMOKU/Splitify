package com.trip.picnic.friends;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GiveDetailsAdapter extends RecyclerView.Adapter<GiveDetailsAdapter.ViewHolder> {

    Context context;
    ArrayList<GiveDetails> arrayActivity;
    String format = "dd/MM/yyyy";

    public GiveDetailsAdapter(Context context, ArrayList<GiveDetails> arrayActivity) {
        this.context = context;
        this.arrayActivity = arrayActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.give_activity, parent, false);
        GiveDetailsAdapter.ViewHolder viewHolder = new GiveDetailsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(arrayActivity.get(position).nameOfGiver);
       // holder.amount.setText(arrayActivity.get(position).amount);

        GiveDBHelper giveDBHelper = new GiveDBHelper(context);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update_adapter);

                ImageView image = dialog.findViewById(R.id.image1);
                EditText edtName = dialog.findViewById(R.id.name);
                EditText edtPhNum = dialog.findViewById(R.id.phNum);
                EditText edtDate = dialog.findViewById(R.id.dayDate3);
                EditText amount = dialog.findViewById(R.id.amount);
                Button action = dialog.findViewById(R.id.updateAction);

                byte[] byteImg = (arrayActivity.get(position)).img;
                int id = (arrayActivity.get(position)).id;
                edtName.setText((arrayActivity.get(position)).nameOfGiver);
                edtPhNum.setText((arrayActivity.get(position)).phNum);
                edtDate.setText((arrayActivity.get(position)).dueDate);
                amount.setText((arrayActivity.get(position)).amount);

                GiveDetails giveDetails = new GiveDetails();
                if(byteImg != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteImg, 0, byteImg.length);
                    image.setImageBitmap(bitmap);
                }

                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = edtName.getText().toString();
                        String phnum= edtPhNum.getText().toString();
                        String date = edtDate.getText().toString();
                        String amount1 = amount.getText().toString();

                        if(!(name.equals("") || phnum.equals("") || date.equals("") || amount1.equals(""))){
                            if(isValidDate(date, format)){

                                GiveDetails giveDetails = new GiveDetails();
                                giveDetails.id = id;
                                giveDetails.nameOfGiver = name;
                                giveDetails.amount = amount1;
                                giveDetails.dueDate = date;
                                giveDetails.phNum = phnum;
                                giveDetails.img = byteImg;
                                giveDBHelper.updateGiver(giveDetails);

                                arrayActivity.set(position, new GiveDetails(id,name,amount1,date,phnum,byteImg));
                                notifyItemChanged(position);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(context, "Enter correct Date",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Don't leave Empty",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to delete "+arrayActivity.get(position).nameOfGiver)
                        .setIcon(R.drawable.delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int id = arrayActivity.get(position).id;
                                giveDBHelper.deleteGiver(id);
                                arrayActivity.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
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


    }

    @Override
    public int getItemCount() {
        return arrayActivity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView amount;
        LinearLayout linearLayout;
        ImageButton deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount=itemView.findViewById(R.id.amount);
            name = itemView.findViewById(R.id.nameInGiveAct);
            linearLayout = itemView.findViewById(R.id.llRow);
            deleteButton = itemView.findViewById(R.id.delete);
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
}
