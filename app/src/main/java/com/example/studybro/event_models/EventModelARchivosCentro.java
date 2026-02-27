package com.example.studybro.event_models;

public class EventModelARchivosCentro {

    public  String nameTarjeta;
    public  String email;

    public String url;

    public String name;

    public String getNameTarjeta() {
        return nameTarjeta;
    }

    public void setNameTarjeta(String nameTarjeta) {
        this.nameTarjeta = nameTarjeta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventModelARchivosCentro(String nombreCentro, String nombreUsuario, String nombreApunte, String url) {
        this.name = nombreApunte;
        this.email = nombreUsuario;;
        this.nameTarjeta = nombreCentro;
        this.url = url;
    }
}
