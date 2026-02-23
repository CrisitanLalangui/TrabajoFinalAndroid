package com.example.studybro;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;



import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventsAdapterArchivos extends RecyclerView.Adapter<EventsAdapterArchivos.SostenDeVistas> {

    //Se implmentan los tres dm´´etodos

    //static
    Context context; // El permiso
    ArrayList<CargaArchivos> archivos;

    Button botonDescargar;
    DownloadManager manager;

    ArrayList<EventModel> centros;



    CardView cardView;


    public EventsAdapterArchivos(Context context, ArrayList<CargaArchivos> events) {
        this.context = context;
        archivos = events;



    }


    @NonNull
    @Override
    public SostenDeVistas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tarjeta_archivos, parent, false);
        return new SostenDeVistas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SostenDeVistas sosten, int position) {

        CargaArchivos archivoActual = archivos.get(position);
        String url = archivoActual.getUrl(); // Asumiendo que CargaArchivos tiene getUrl()
        String nombreArchivo = archivoActual.getName();
        String nombreTarjeta = archivoActual.getNameTarjeta();



        sosten.tituloApunte.setText(nombreArchivo);

        sosten.nombreTarjeta.setText(nombreTarjeta);

        sosten.botonApunte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                String url = archivoActual.getUrl();
                String baseUrl = "http://10.0.2.2:8000/media/";
                String urlCompleta =  baseUrl + url;

                Uri uri = Uri.parse(urlCompleta);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, archivoActual.getName() + ".pdf");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.allowScanningByMediaScanner();

                manager.enqueue(request);
                android.widget.Toast.makeText(context, "Descarga iniciada. Abriendo descargas...", android.widget.Toast.LENGTH_SHORT).show();

                // --- ABRIR EL EXPLORADOR DE ARCHIVOS ---


            }

        }
        );

    }

    @Override
    public int getItemCount() {
        return archivos.size();
    }


    public class SostenDeVistas extends RecyclerView.ViewHolder {
        TextView nombreTarjeta, tituloApunte,botonApunte;
       ;
        CardView card;


        public SostenDeVistas(@NonNull View itemView) {

            super(itemView);
            nombreTarjeta = itemView.findViewById(R.id.tituloTarjetaApunte);
            card = itemView.findViewById(R.id.cardview);
            tituloApunte  = itemView.findViewById(R.id.tituloApunte);
            botonApunte = itemView.findViewById(R.id.botonDescargarApunte);




        }

    }

}






