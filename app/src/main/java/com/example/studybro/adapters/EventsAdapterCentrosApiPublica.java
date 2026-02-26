package com.example.studybro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybro.R;
import com.example.studybro.activities.PaginadorActivity;
import com.example.studybro.event_models.EventModelCentrosApiPública;

import java.util.ArrayList;

public class EventsAdapterCentrosApiPublica
        extends RecyclerView.Adapter<EventsAdapterCentrosApiPublica.SostenDeVistas> {

    Context context;
    ArrayList<EventModelCentrosApiPública> centros;

    public EventsAdapterCentrosApiPublica(Context context,
                                          ArrayList<EventModelCentrosApiPública> events) {
        this.context = context;
        this.centros = events;
    }

    @NonNull
    @Override
    public SostenDeVistas onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.tarjeta, parent, false);

        return new SostenDeVistas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SostenDeVistas holder,
                                 int position) {

        EventModelCentrosApiPública centro = centros.get(position);

        holder.nombreCentro.setText(centro.getNombreCentro());
        holder.tvTipoInsituto.setText(centro.getTipoEducacion());
        holder.location.setText(centro.getLocalizacion());
        holder.tipoAccesibilidad.setText(centro.getTipoAccesibilidad());


        holder.card.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if(pos == RecyclerView.NO_POSITION) return;

            Intent intent = new Intent(
                    v.getContext(),
                    PaginadorActivity.class
            );

            intent.putExtra(
                    "nombreTarjeta",
                    centros.get(pos).getNombreCentro()
            );

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return centros.size();
    }

    public static class SostenDeVistas
            extends RecyclerView.ViewHolder {

        TextView nombreCentro;
        TextView tipoAccesibilidad;
        TextView location;
        TextView tvTipoInsituto;
        CardView card;

        public SostenDeVistas(@NonNull View itemView) {
            super(itemView);

            nombreCentro = itemView.findViewById(R.id.titulocentro);
            location = itemView.findViewById(R.id.ubicacion);
            card = itemView.findViewById(R.id.cardview);
            tipoAccesibilidad = itemView.findViewById(R.id.tipoAccesibilidad);
            tvTipoInsituto = itemView.findViewById(R.id.tipoInstituto);
        }
    }
}