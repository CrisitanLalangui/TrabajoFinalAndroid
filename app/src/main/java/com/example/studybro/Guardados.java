package com.example.studybro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Guardados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guardados);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainMenuPrincipalGuardados), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ConstraintLayout desplegable = findViewById(R.id.seccionDEsplegableguardados);


        ImageButton menuHamburguesa = findViewById(R.id.iconohamburegesaGuardados);

        menuHamburguesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                desplegable.setVisibility(View.VISIBLE);
            }


        });


        ImageButton menuHamburguesaDesplegable = findViewById(R.id.desplegableHamguresaGuardados);

        menuHamburguesaDesplegable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                desplegable.setVisibility(View.GONE);
            }


        });


        LinearLayout seccionMenuPrincipal = findViewById(R.id.seccionMenuPrincipal);

        LinearLayout seccionGuardados = findViewById(R.id.seccionGuardados);

        LinearLayout secccionApuntes = findViewById(R.id.seccionApuntes);

        LinearLayout seccionSesion = findViewById(R.id.seccionSesion);


        seccionMenuPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Guardados.this, MainActivity.class);
                startActivity(intent);

            }
        });

        seccionGuardados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Guardados.this, Guardados.class);
                startActivity(intent);


            }
        });

        secccionApuntes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Guardados.this, MisApuntes.class);
                startActivity(intent);


            }
        });

        seccionSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Guardados.this, Login.class);
                startActivity(intent);

            }
        });

    }
}