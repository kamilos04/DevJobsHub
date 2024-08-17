package com.kamiljach.devjobshub;

import java.util.ArrayList;

public class UtilityClass {
    public static ArrayList<Long> removeRepetitionLong(ArrayList<Long> list){
        ArrayList<Long> newList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            if(!newList.contains(list.get(i))){
                newList.add(list.get(i));
            }
        }
        return newList;
    }
}
