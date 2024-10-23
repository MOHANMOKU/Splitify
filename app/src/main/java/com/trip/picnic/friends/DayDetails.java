package com.trip.picnic.friends;

import java.util.ArrayList;
import java.util.Map;

public class DayDetails {


    int id;
    int numOfMembers;
    Map<String, Integer> expenses;
    Map<String, Integer> paid;
    ArrayList<String> names;
    ArrayList<String> places;
    String date;


    public DayDetails(int numOfMembers, String date){
        this.numOfMembers = numOfMembers;
        this.date = date;

    }
    public DayDetails(int id, int numOfMembers, Map<String, Integer> expenses, Map<String, Integer> paid, ArrayList<String> names, ArrayList<String> places, String date) {
        this.id = id;
        this.numOfMembers = numOfMembers;
        this.expenses = expenses;
        this.paid = paid;
        this.names = names;
        this.places = places;
        this.date = date;
    }

}
