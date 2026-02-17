package com.example.studybro;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterfaz {

    @GET("egob/catalogo/300614-0-centros-educativos.json") // endpoint de DRagonBall Url
    Call<Centros> getCentros(

    );

    class
    Archivo {
        private String name;
        private String email;
        private String url;

        public Archivo (String name, String email, String url) {
            this.name = name;
            this.email = email;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getUrl() {
            return url;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

