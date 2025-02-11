package com.finki.messageshoot.View.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.finki.messageshoot.databinding.ActivityMainBinding;

public class CustomFragmentManager {

    @SuppressLint("CommitTransaction")
    public static void changeFragment(AppCompatActivity appCompatActivity, ActivityMainBinding binding, Fragment fragment, boolean shouldPutOnStack){
        FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
        if (shouldPutOnStack){
            fragmentManager.beginTransaction()
                    .replace(binding.fragmentContainerViewMainActivity.getId(), fragment)
                    .addToBackStack("stack")
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(binding.fragmentContainerViewMainActivity.getId(), fragment)
                    .commit();
        }
    }

}
