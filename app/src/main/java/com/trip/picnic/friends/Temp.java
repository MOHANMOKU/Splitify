package com.trip.picnic.friends;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Map;

@Entity(tableName = "Temp")
@TypeConverters(Type_Converters.class)
public class Temp {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "number")
    public int number;

    @ColumnInfo(name = "NameArray")
    @TypeConverters(Type_Converters.class)
    public ArrayList<String> names;

    @ColumnInfo(name = "PlaceArray")
    @TypeConverters(Type_Converters.class)
    public ArrayList<String> places;

    @ColumnInfo(name = "Expenses")
    @TypeConverters(Type_Converters.class)
    public Map<String, Integer> expenses;

    @ColumnInfo(name = "Paid")
    @TypeConverters(Type_Converters.class)
    public Map<String, Integer> paid;

    @ColumnInfo(name = "Date")
    public String date;

    public Temp(int id, int number, Map<String, Integer> expenses, Map<String, Integer> paid, ArrayList<String> names, ArrayList<String> places, String date) {
        this.id = id;
        this.number = number;
        this.expenses = expenses;
        this.paid = paid;
        this.names = names;
        this.places = places;
        this.date = date;
    }
    @Ignore
    public Temp(int number, Map<String, Integer> expenses, Map<String, Integer> paid, ArrayList<String> names, ArrayList<String> places, String date) {
        this.number = number;
        this.expenses = expenses;
        this.paid = paid;
        this.names = names;
        this.places = places;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Map<String, Integer> getExpenses() {
        return expenses;
    }

    public void setExpenses(Map<String, Integer> expenses) {
        this.expenses = expenses;
    }

    public Map<String, Integer> getPaid() {
        return paid;
    }

    public void setPaid(Map<String, Integer> paid) {
        this.paid = paid;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public ArrayList<String> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<String> places) {
        this.places = places;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
