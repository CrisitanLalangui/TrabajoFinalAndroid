package com.example.studybro.models;

import com.google.gson.annotations.SerializedName;

public class CentrosDjangoObtener {

    @SerializedName("name")
    private String nombreCentro;



    public String getNombreCentro() {
        return nombreCentro;
    }

    public CentrosDjangoObtener(String nombreCentro,String slugCentro) {
        this.nombreCentro = nombreCentro;
    }

    public void setNombreCentro(String nombreCentro) {
        this.nombreCentro = nombreCentro;
    }
}
