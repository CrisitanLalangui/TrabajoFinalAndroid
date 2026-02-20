package com.example.studybro;

import androidx.annotation.NonNull;

public class EventModel {

    public String nombreCentro;

    public String Localizacion;

    public String tipoEducacion;

    public String tipoAccesibilidad;

    public String id;


    public EventModel(String nombreCentro, String Localizaconi, String tipoEducacion, String tipoAccesibilidad,String id) {
        this.nombreCentro = nombreCentro;
        this.Localizacion = Localizaconi;
        this.tipoEducacion = tipoEducacion;
        this.tipoAccesibilidad = tipoAccesibilidad;
        this.id = id;

    }

    public String getNombreCentro() {
        return nombreCentro;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }


    public String getLocalizacion() {
        return Localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.Localizacion = localizacion;
    }

    public String getTipoEducacion() {
        return tipoEducacion;
    }

    public void setTipoEducacion(String tipoEducacion) {
        this.tipoEducacion = tipoEducacion;
    }

    @NonNull
    @Override
    public String toString() {
        return nombreCentro + " " + Localizacion + " " + tipoEducacion;
    }



    public String getTipoAccesibilidad() {
        return tipoAccesibilidad;
    }

    public void setTipoAccesibilidad(String tipoAccesibilidad) {
        this.tipoAccesibilidad = tipoAccesibilidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
