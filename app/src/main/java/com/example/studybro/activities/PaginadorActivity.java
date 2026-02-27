package com.example.studybro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
        ImageButton imageButton;
        imageButton = findViewById(R.id.botonSalirCentro);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PaginadorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        TextView tituloPagina;
        tituloPagina = findViewById(R.id.titulo);
        tituloPagina.setText(getIntent().getStringExtra("nombreTarjeta"));






        Paginador adapter =
                new Paginador(getSupportFragmentManager());

        adapter.setBundle(getIntent().getExtras());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);


    }
}