package com.example.studybro;

public class CargaArchivos {



    String name;

    String url;

    String email;

    String nameTarjeta;

    public CargaArchivos(String name, String url, String email, String nameTarjeta) {
        this.name = name;
        this.url = url;
        this.email = email;
        this.nameTarjeta = nameTarjeta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameTarjeta() {
        return nameTarjeta;
    }

    public void setNameTarjeta(String nameTarjeta) {
        this.nameTarjeta = nameTarjeta;
    }
}
