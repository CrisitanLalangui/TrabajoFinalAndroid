package com.example.studybro.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.studybro.R;
import com.example.studybro.adapters.EventsAdapterCentros;
import com.example.studybro.apis.ApiCliente;
import com.example.studybro.apis.ApiInterfaz;
import com.example.studybro.event_models.EventModelCentros;
import com.example.studybro.models.Centros;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<EventModelCentros> CentrosOriginales = new ArrayList<>();
    ArrayList<EventModelCentros> CentrosCopia = new ArrayList<>();
    EventsAdapterCentros adaptador;

    SpinKitView spinKitView;

    private void filtrar(String texto) {
        try {
            CentrosOriginales.clear();
            if (texto.isEmpty()) {
                CentrosOriginales.addAll(CentrosCopia);
            } else {
                String query = texto.toLowerCase().trim();
                for (EventModelCentros event : CentrosCopia) {
                    if (event.getNombreCentro() != null && event.getNombreCentro().toLowerCase().contains(query)) {
                        CentrosOriginales.add(event);
                    }
                }
            }
            if (adaptador != null) {
                adaptador.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("FILTRO_ERROR", "Error al filtrar: " + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        try {


            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main);

            spinKitView = findViewById(R.id.spin_kitAnimation);

            spinKitView.setVisibility(VISIBLE);

            ImageButton iconoPersona = findViewById(R.id.iconoPersonaPrincipal);

            iconoPersona.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PerfilUsuario.class);
                    startActivity(intent);
                }

            });

            ConstraintLayout desplegable = findViewById(R.id.seccionDesplegablePrincpoal);

            ImageButton menuHamburguesa = findViewById(R.id.iconohamburegesaPrincipal);

            menuHamburguesa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    desplegable.setVisibility(VISIBLE);

                }
            });

            ImageButton menuHamburguesaDesplegable = findViewById(R.id.desplegablePrincipal);

            menuHamburguesaDesplegable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    desplegable.setVisibility(GONE);
                }

            });


            LinearLayout seccionMenuPrincipal = findViewById(R.id.seccionMenuPrincipal);

            LinearLayout seccionGuardados = findViewById(R.id.seccionGuardados);

            LinearLayout secccionApuntes = findViewById(R.id.seccionApuntes);

            LinearLayout seccionSesion = findViewById(R.id.seccionSesion);


            seccionMenuPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            });

            seccionGuardados.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, Guardados.class);
                    startActivity(intent);
                }
            });

            secccionApuntes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, MisApuntes.class);
                    startActivity(intent);

                }
            });

            seccionSesion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);

                }
            });

            // Verifica que este ID exista en activity_main.xml
            if (findViewById(R.id.mainMenuPrincipal) != null) {
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainMenuPrincipal), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
            }

            recyclerView = findViewById(R.id.historicEventsARchivosRecycle);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            adaptador = new EventsAdapterCentros(this, CentrosOriginales);
            recyclerView.setAdapter(adaptador);

            // Buscador
            TextInputEditText editText = findViewById(R.id.buscador);
            if (editText != null) {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        filtrar(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }

            cargarDatos();

        } catch (Exception e) {
            Log.e("CRASH_ON_CREATE", e.getMessage());
            Toast.makeText(this, "Error al iniciar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void cargarDatos() {

        spinKitView.setVisibility(VISIBLE);
        ApiInterfaz apiInterface = ApiCliente.getClient().create(ApiInterfaz.class);
        Call<Centros> call = apiInterface.getCentros();

        call.enqueue(new Callback<Centros>() {
            @Override
            public void onResponse(Call<Centros> call, Response<Centros> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        Centros centrosListado = response.body();
                        if (centrosListado.centrosMadrid == null) return;

                        ArrayList<EventModelCentros> temp = new ArrayList<>();
                        int limite = Math.min(50, centrosListado.centrosMadrid.size());

                        for (int i = 0; i < limite; i++) {
                            Centros.CentroMadrid center = centrosListado.centrosMadrid.get(i);

                            String nombreCentro = center.nombreCentro != null ? center.nombreCentro : "Sin nombre";
                            String calle = (center.address != null && center.address.streetName != null) ? center.address.streetName : "S/N";
                            String id = center.id != null ? center.id : "N/A";


                            String organizacion = "N/A", descendencia = "N/A";

                            if (center.organization != null) {
                                organizacion = center.organization.organizationName != null ? center.organization.organizationName : "N/A";
                                descendencia = center.organization.organizationDesc != null ? center.organization.organizationDesc : "N/A";

                                if (organizacion.toLowerCase().contains("public") || organizacion.toLowerCase().contains("públic")) {
                                    organizacion = "Público";
                                } else if (organizacion.toLowerCase().contains("municipal") || organizacion.toLowerCase().contains("madrid")) {
                                    organizacion = "Público";
                                } else
                                    organizacion = "Privado";

                                if (descendencia.toLowerCase().contains("bachillerato")) {
                                    descendencia = "Bachillerato";
                                } else if (descendencia.toLowerCase().contains("primaria")) {
                                    descendencia = "Primaria";
                                } else if (descendencia.toLowerCase().contains("infantil")) {
                                    descendencia = "infantil";
                                } else if (descendencia.toLowerCase().contains("uni")) {
                                    descendencia = "Universidad";
                                } else {
                                    descendencia = "insituto";

                                }
                            }

                            temp.add(new EventModelCentros(nombreCentro, calle, descendencia, organizacion,id));
                        }

                        // Actualizar listas y UI
                        CentrosOriginales.clear();
                        CentrosCopia.clear();
                        CentrosOriginales.addAll(temp);
                        CentrosCopia.addAll(temp);
                        adaptador.notifyDataSetChanged();
                        spinKitView.setVisibility(GONE);

                    } catch (Exception e) {
                        Log.e("ERROR_PROCESANDO", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Centros> call, Throwable t) {
                spinKitView.setVisibility(GONE);
                Log.e("Error de conexión", t.getMessage());
            }
        });
    }
}