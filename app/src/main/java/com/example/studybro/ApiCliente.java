    package com.example.studybro;


    import okhttp3.OkHttpClient;
    import okhttp3.logging.HttpLoggingInterceptor;
    import retrofit2.Retrofit;
    import retrofit2.converter.gson.GsonConverterFactory;

    public class ApiCliente {


        public static String BASE_URL_CENTROS = "https://datos.madrid.es/";
        public static String BASE_URL_EMULADOR = "http://10.0.2.2:8000/"; //Esto es un enlace web, que permite acceder
                                                                  // desde cualquier dispositivo a djang
        public static Retrofit retrofitCentros;
        public static Retrofit retrofitEmulador;

        public static Retrofit getClient() {
            if (retrofitCentros == null) {
                // Configuración del interceptor para ver los logs de las peticiones
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                // Configuración del cliente OkHttp
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                // Configuración de Retrofit
                retrofitCentros = new Retrofit.Builder()
                        .baseUrl(BASE_URL_CENTROS)
                        .addConverterFactory(GsonConverterFactory.create()) // Se agrega el conversor (usualmente Gson)
                        .client(client)
                        .build();
            }
            return retrofitCentros;
        }

        public static Retrofit getClient2() {
            if (retrofitEmulador == null) {
                // Configuración del interceptor para ver los logs de las peticiones
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                // Configuración del cliente OkHttp
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .build();

                // Configuración de Retrofit
                retrofitEmulador = new Retrofit.Builder()
                        .baseUrl(BASE_URL_EMULADOR)
                        .addConverterFactory(GsonConverterFactory.create()) // Se agrega el conversor (usualmente Gson)
                        .client(client)
                        .build();
            }
            return retrofitEmulador;
        }
    }


