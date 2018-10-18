package com.jagdish.bakingapp.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class BaseFragment extends Fragment {

    public static void addFragment(Context context, int containerid, Fragment fragment, String tag, String stackName) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerid, fragment, tag);
        fragmentTransaction.addToBackStack(stackName);
        fragmentTransaction.commit();
    }

    public static void replaceFragment(Context context, int containerid, Fragment fragment, String tag) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerid, fragment, tag);
        fragmentTransaction.commit();
    }


}

