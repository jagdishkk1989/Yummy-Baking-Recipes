package com.jagdish.bakingapp.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jagdish.bakingapp.BakingApplication;


public class SessionManager {

    private static final String PREFS_NAME = "BakingApplication";

    public static void setIntegerSharedPrefs(String key, int value) {
        Editor editor = BakingApplication.getContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntegerSharedPrefs(String key) {
        SharedPreferences prefs = BakingApplication.getContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, 1);
    }

}
