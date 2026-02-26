package com.example.studybro.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArchivosCentro {


    @SerializedName("data")
    public List<archivosDelCentro> archivosCentro;

    public static class archivosDelCentro {

        @SerializedName("name")
        public String name;

        @SerializedName("nombreTarjeta")
        public String nombreTarjeta;

        @SerializedName("url")
        public String url;

        @SerializedName("email")
        public String email;

    }

}
