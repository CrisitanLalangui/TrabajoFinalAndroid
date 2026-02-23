package com.example.studybro;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterfaz {

    @GET("egob/catalogo/300614-0-centros-educativos.json")
    Call<Centros> getCentros();
    @POST("api/studybro/loginStudyBro/")
    Call<ResponseBody> iniciarSesion(@Body UsuarioLogin usuarioLogin);
    @POST("api/studybro/registroStudyBro/")
    Call<ResponseBody> registrarUsuario(@Body UsuarioRegistro usuarioRegistro);

    @Multipart
    @POST("api/studybro/ligarArchivosStudyBro/")
    Call<ResponseBody> subirApuntes(

            @Part MultipartBody.Part url,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("nombreTarjeta") RequestBody nombreTarjeta
            );
    @GET("api/studybro/consultarArchivosStudyBro/")
    Call<Archivo> consultarArchivos(

            @Query("email") String email


    );




}

