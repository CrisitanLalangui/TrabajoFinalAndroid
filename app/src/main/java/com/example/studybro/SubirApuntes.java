package com.example.studybro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubirApuntes extends AppCompatActivity {

    private static final String TAG = "DEBUG_STUDYBRO";
    private byte[] bytesArchivo = null;
    private String nombreArchivo = "archivo_estudiante.pdf";
    private String mimeType = "application/pdf";
    private TextView ueri;

    // Selector de archivos moderno
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    leerArchivoInmediatamente(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subir_apuntes);

        ueri = findViewById(R.id.ueriApunte);
        Button botonSubir = findViewById(R.id.botonSubirApunte);
        Button botonGuardar = findViewById(R.id.botonGuardarApuntes);
        TextView inputNombre = findViewById(R.id.textInputLayoutnombreApunte);

        botonSubir.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        });

        botonGuardar.setOnClickListener(v -> {
            if (bytesArchivo == null || bytesArchivo.length == 0) {
                Toast.makeText(this, "Error: Selecciona un archivo primero", Toast.LENGTH_SHORT).show();
                return;
            }
            String nombreParaDjango = inputNombre.getText().toString().trim();
            if (nombreParaDjango.isEmpty()) nombreParaDjango = "Apunte_Sin_Nombre";

            subir(nombreParaDjango);
        });
    }

    private void leerArchivoInmediatamente(Uri uri) {
        try (InputStream is = getContentResolver().openInputStream(uri)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int len;
            while ((len = is.read(buffer)) != -1) bos.write(buffer, 0, len);

            bytesArchivo = bos.toByteArray();
            mimeType = getContentResolver().getType(uri);
            if (mimeType == null) mimeType = "application/octet-stream";



            // Obtener nombre real
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) nombreArchivo = cursor.getString(nameIndex);
                }
            }

            ueri.setText("Archivo cargado: " + bytesArchivo.length + " bytes");
            Log.d(TAG, "Archivo en memoria. Nombre: " + nombreArchivo);
        } catch (Exception e) {
            Log.e(TAG, "Error al leer: " + e.getMessage());
            Toast.makeText(this, "Error al procesar el archivo local", Toast.LENGTH_SHORT).show();
        }
    }

    private void subir(String nombre) {


        // 1. Obtener datos persistentes
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String email = preferences.getString("email", null);





        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

        if (email == null){

            Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        }


        Bundle bundle = getIntent().getExtras();
        String idTarjeta = (bundle != null && bundle.getString("id") != null) ? bundle.getString("id") : "0";

        if (idTarjeta == null){
            idTarjeta = "0";
            Toast.makeText(this, idTarjeta, Toast.LENGTH_SHORT).show();
        }


        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), bytesArchivo);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("url", nombreArchivo, requestFile);


        RequestBody reqName = RequestBody.create(MediaType.parse("text/plain"), nombre);
        RequestBody reqEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody reqId = RequestBody.create(MediaType.parse("text/plain"), idTarjeta);


        ApiInterfaz api = ApiCliente.getClient2().create(ApiInterfaz.class);
        Call<ResponseBody> call = api.subirApuntes(filePart, reqName, reqEmail, reqId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Subida exitosa");
                    Toast.makeText(SubirApuntes.this, "¡Archivo subido correctamente!", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    try {
                        String error = response.errorBody().string();
                        Log.e(TAG, "Error servidor: " + error);
                        Toast.makeText(SubirApuntes.this, "Error Django: " + error, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "Error al leer cuerpo de error");
                    }

                }

            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Fallo total: " + t.getMessage());
                Toast.makeText(SubirApuntes.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}