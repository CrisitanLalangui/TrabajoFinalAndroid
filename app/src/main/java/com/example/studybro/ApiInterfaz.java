package com.example.studybro;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterfaz {

    @GET("egob/catalogo/300614-0-centros-educativos.json")
    Call<Centros> getCentros();
    @POST("api/studybro/loginStudyBro/")
    Call<ResponseBody> iniciarSesion(@Body UsuarioLogin usuarioLogin);
    @POST("api/studybro/registroStudyBro/")
    Call<ResponseBody> registrarUsuario(@Body UsuarioRegistro usuarioRegistro);


}

