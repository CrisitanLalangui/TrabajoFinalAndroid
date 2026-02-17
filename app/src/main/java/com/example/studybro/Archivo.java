package com.example.studybro;

public class Archivo {

    String name;
    String url;

    String email;

    public Archivo(String name, String url, String email) {
        this.name = name;
        this.url = url;
        this.email = email;
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
}
