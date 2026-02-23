package com.example.studybro;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class degradadoTitulo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tarjeta_archivos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainMisApuntes), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        TextView textView = findViewById(R.id.tituloTarjetaApunte);
        LinearGradient gradienteLineal = new LinearGradient(
                0, 0, 0, textView.getTextSize(),
                new int[]{Color.parseColor("#BF953F"), Color.parseColor("#FCF6BA"), Color.parseColor("#B38728")},
                null,

                Shader.TileMode.CLAMP
        );
        textView.getPaint().setShader(gradienteLineal);

    }
}