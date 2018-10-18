package com.jagdish.bakingapp;

import android.app.Application;


public class BakingApplication extends Application {

    public static final String TAG = BakingApplication.class.getName();

    private static BakingApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public BakingApplication() {
        // TODO Auto-generated constructor stub
        instance = this;
    }

    public static BakingApplication getContext() {
        if (instance == null) {
            instance = new BakingApplication();
        }
        return instance;
    }
}
