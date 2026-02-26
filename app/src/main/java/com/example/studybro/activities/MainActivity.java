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
import com.example.studybro.adapters.EventsAdapterCentrosApiPublica;
import com.example.studybro.apis.ApiCliente;
import com.example.studybro.apis.ApiInterfaz;
import com.example.studybro.event_models.EventModelCentrosApiPública;
import com.example.studybro.models.Centros;
import com.example.studybro.models.CentrosDjango;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;


    ArrayList<EventModelCentrosApiPública> CentrosOriginales = new ArrayList<>();
    ArrayList<EventModelCentrosApiPública> CentrosCopia = new ArrayList<>();
    EventsAdapterCentrosApiPublica adaptador;

    SpinKitView spinKitView;

    private void filtrar(String texto) {
        try {
            CentrosOriginales.clear();
            if (texto.isEmpty()) {
                CentrosOriginales.addAll(CentrosCopia);
            } else {
                String query = texto.toLowerCase().trim();
                for (EventModelCentrosApiPública event : CentrosCopia) {
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

            adaptador = new EventsAdapterCentrosApiPublica(this, CentrosOriginales);
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

                        ArrayList<EventModelCentrosApiPública> temp = new ArrayList<>();
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

                            temp.add(new EventModelCentrosApiPública(nombreCentro, calle, descendencia, organizacion,id));
                        }

                        // Actualizar listas y UI
                        CentrosOriginales.clear();
                        CentrosCopia.clear();
                        CentrosOriginales.addAll(temp);
                        CentrosCopia.addAll(temp);
                        adaptador.notifyDataSetChanged();
                        spinKitView.setVisibility(GONE);
                        for( int i = 0; i< CentrosOriginales.size();i++) {

                            CentrosDjango centrosDjango = new CentrosDjango(CentrosOriginales.get(i).getNombreCentro());
                            ApiInterfaz apiInterfaz = ApiCliente.getClient2().create(ApiInterfaz.class);
                            Call<ResponseBody> call2 = apiInterfaz.registrarCentros(centrosDjango);



                            call2.enqueue(new Callback<ResponseBody>() {


                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if (response.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Toast.makeText(MainActivity.this, "NO se han creado los centros en django", Toast.LENGTH_SHORT).show();
                                    }
                                }


                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    Toast.makeText(MainActivity.this, "Ha habido un error  en el registro de centros", Toast.LENGTH_SHORT).show();
                                }

                            });

                        }


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

        /**
         * UsuarioRegistro usuarioRegistro = new UsuarioRegistro(correoElectronico, contraseña, confirmarContraseña);
         *                     ApiInterfaz api = ApiCliente.getClient2().create(ApiInterfaz.class);
         *                     //El response Body es para enviar una respuesta al servidor
         *                     Call<ResponseBody> call = api.registrarUsuario(usuarioRegistro);
         *
         *                     call.enqueue(new Callback<ResponseBody>() {
         *                         @Override
         *                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
         *
         *
         *                             if (response.isSuccessful()) {
         *                                 Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
         *
         *                                 Intent intentMain = new Intent(Register.this, Login.class);
         *                                 startActivity(intentMain);
         *                             } else {
         *
         *
         *                                 try {
         *                                     String error = response.errorBody().string();
         *                                     error = error.replace("{\"success\":false,\"errors\":[\"", "");
         *                                     error = error.replace("\"}", "");
         *                                     Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();
         *
         *                                 } catch (IOException e) {
         *                                     throw new RuntimeException(e);
         *                                 }
         *                             }
         *                         }
         *
         *                         @Override
         *                         public void onFailure(Call<ResponseBody> call, Throwable t) {
         *
         *                             Toast.makeText(Register.this, "Ha habido un error  en el registro", Toast.LENGTH_SHORT).show();
         *                         }
         *                     });
         */




    }



}