package com.example.studybro.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybro.R;
import com.example.studybro.adapters.EventsAdapterArchivos;
import com.example.studybro.adapters.EventsAdapterArchivosCentro;
import com.example.studybro.apis.ApiCliente;
import com.example.studybro.apis.ApiInterfaz;
import com.example.studybro.event_models.EventModelARchivosCentro;
import com.example.studybro.event_models.EventModelArchivos;
import com.example.studybro.models.Archivo;
import com.example.studybro.models.ArchivosCentro;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApuntesDelCentro extends Fragment {

    private static final String TAG = "DEBUG_CENTRO";

    private ArrayList<EventModelARchivosCentro> archivosMisApuntes = new ArrayList<>();
    private ArrayList<EventModelARchivosCentro> copiaArchivosMisApuntes = new ArrayList<>();

    private EventsAdapterArchivosCentro adaptador;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.activity_apuntes_del_centro,
                container,
                false
        );

        RecyclerView recyclerView =
                rootView.findViewById(R.id.historicEventsARchivosRecycle);

        adaptador = new EventsAdapterArchivosCentro(getContext(),archivosMisApuntes);


        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        recyclerView.setAdapter(adaptador);

        // 🔹 Obtener nombre del centro
        Bundle bundle = getArguments();

        if (bundle == null || !bundle.containsKey("nombreTarjeta")) {
            Toast.makeText(getContext(),
                    "No se recibió el centro",
                    Toast.LENGTH_LONG).show();
            return rootView;
        }

        String nombreTarjeta = bundle.getString("nombreTarjeta");

        cargarArchivos(nombreTarjeta);

        return rootView;
    }

    private void cargarArchivos(String nombreTarjeta) {

        ApiInterfaz api =
                ApiCliente.getClient2().create(ApiInterfaz.class);

        Call<ArchivosCentro> call =
                api.recogerArchivos(nombreTarjeta.trim());

        call.enqueue(new Callback<ArchivosCentro>() {
            @Override
            public void onResponse(Call<ArchivosCentro> call, Response<ArchivosCentro> response) {

                ArrayList<String> email = new ArrayList<>();
                ArrayList<String> nameTarjeta = new ArrayList<>();
                ArrayList<String> url = new ArrayList<>();
                ArrayList<String> name = new ArrayList<>();

                ArchivosCentro archivosCentro = response.body();

                for (ArchivosCentro.archivosDelCentro fileCenter : archivosCentro.archivosCentro) {
                    email.add(fileCenter.fileDetailCentro.email);
                    nameTarjeta.add(fileCenter.fileDetailCentro.nombreTarjeta);
                    url.add(fileCenter.fileDetailCentro.url);
                    name.add(fileCenter.fileDetailCentro.name);
                }
                for (int i = 0; i < email.size(); i++) {
                    archivosMisApuntes.add(new EventModelARchivosCentro(nameTarjeta.get(i), email.get(i), name.get(i),url.get(i)));
                        copiaArchivosMisApuntes.add(new EventModelARchivosCentro(name.get(i), email.get(i), nameTarjeta.get(i),url.get(i)));

                }
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArchivosCentro> call, Throwable t) {

                Toast.makeText(getContext(), "Error al cargar los archivos del centro", Toast.LENGTH_SHORT).show();

            }
        });

    }
}