package com.example.krishbhatia.instagramclone.Utils;

import android.util.Log;

public class StringManipulation {
    private static final String TAG = "StringManipulation";
    public static String expandingUsername(String username){
        Log.d(TAG, "expandingUsername: "+ username.replace("."," "));
        return username.replace("."," ");
    }

    public static String condensingUsername(String username){

        Log.d(TAG, "condensingUsername: "+username.replace(" ","."));return username.replace(" ",".");
    }
}
