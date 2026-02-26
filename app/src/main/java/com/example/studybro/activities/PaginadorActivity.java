package com.example.studybro.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.studybro.R;

public class PaginadorActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paginador);

        viewPager = findViewById(R.id.mainPaginador);

        Paginador adapter =
                new Paginador(getSupportFragmentManager());

        adapter.setBundle(getIntent().getExtras());

        viewPager.setAdapter(adapter);
    }
}