package com.example.studybro.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Archivo {


    @SerializedName("data")
    public List<File> filesList;

    public static class File {

        @SerializedName("archivo")
        public FileDetail fileDetail;
    }

    public static class FileDetail {

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