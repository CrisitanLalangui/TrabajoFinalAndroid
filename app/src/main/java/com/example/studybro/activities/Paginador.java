package com.example.studybro.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.example.studybro.activities.ApuntesDelCentro;
import com.example.studybro.activities.subirApuntes2;

import android.os.Bundle;
import androidx.fragment.app.FragmentStatePagerAdapter;




public class Paginador extends FragmentStatePagerAdapter  {

    private Bundle bundle;

    public Paginador(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    // Recibir datos del Intent
    public void setBundle(Bundle b){
        bundle = b;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        if(position == 0){
            fragment = new subirApuntes2();
        }

        else{
            fragment = new ApuntesDelCentro();
        }

        if(bundle != null){
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}