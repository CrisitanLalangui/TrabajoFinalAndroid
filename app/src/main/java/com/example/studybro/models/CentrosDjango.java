package com.example.studybro.models;

import com.google.gson.annotations.SerializedName;

public class CentrosDjango {

    @SerializedName("name")
    private String nombreCentro;

    public String getNombreCentro() {
        return nombreCentro;
    }

    public CentrosDjango(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }
}
