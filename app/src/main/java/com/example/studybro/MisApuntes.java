package com.example.studybro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisApuntes extends AppCompatActivity {




    RecyclerView recyclerView;
    ArrayList<CargaArchivos> archivosMisApuntes = new ArrayList<>();
    ArrayList<CargaArchivos> copiaArchivosMisApuntes = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mis_apuntes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainMenuPrincipalApuntes), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.historicEventsARchivosRecycle);

        EventsAdapterArchivos adaptador = new EventsAdapterArchivos(this, archivosMisApuntes);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);


        ApiInterfaz apiInterface = ApiCliente.getClient2().create(ApiInterfaz.class);

        Call<Archivo> call = apiInterface.consultarArchivos();



        call.enqueue(new Callback<Archivo>() {


            @Override
            public void onResponse(Call<Archivo> call, Response<Archivo> response) {

                ArrayList<String> email = new ArrayList<>();
                ArrayList<String> nameTarjeta = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();

                Archivo archivos = response.body();

                for (Archivo.File f : archivos.filesList) {
                    email.add(f.fileDetail.email);
                    nameTarjeta.add(f.fileDetail.nombreTarjeta);
                    url.add(f.fileDetail.url);
                    name.add(f.fileDetail.name);
                }


                for (int i = 0; i < email.size(); i++) {
                    archivosMisApuntes.add(new CargaArchivos(name.get(i), url.get(i), email.get(i), nameTarjeta.get(i)));
                    copiaArchivosMisApuntes.add(new CargaArchivos(name.get(i), url.get(i), email.get(i), nameTarjeta.get(i)));

                }
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Archivo> call, Throwable t) {

                Toast.makeText(MisApuntes.this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();

            }

        });


        ImageButton iconoPersona = findViewById(R.id.iconoPersonaApuntes);

        iconoPersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisApuntes.this, PerfilUsuario.class);
                startActivity(intent);
            }

            });


        ConstraintLayout desplegable = findViewById(R.id.seccionDEsplegable);

        ImageButton menuHamburguesa = findViewById(R.id.iconoHamburguesaApuntes);

        menuHamburguesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                desplegable.setVisibility(View.VISIBLE);
            }

        });


        ImageButton menuHamburguesaDesplegable = findViewById(R.id.desplegableHamburegesaPerfil);

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

                Intent intent = new Intent(MisApuntes.this, MainActivity.class);
                startActivity(intent);

            }
        });

        seccionGuardados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MisApuntes.this, Guardados.class);
                startActivity(intent);

            }
        });

        secccionApuntes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MisApuntes.this, MisApuntes.class);
                startActivity(intent);


            }
        });

        seccionSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MisApuntes.this, Login.class);
                startActivity(intent);


            }
        });


    }
}