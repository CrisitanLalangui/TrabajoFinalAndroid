package com.example.studybro.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybro.R;
import com.example.studybro.apis.ApiCliente;
import com.example.studybro.apis.ApiInterfaz;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class subirApuntes2 extends Fragment {

    private static final String TAG = "DEBUG_STUDYBRO";

    private byte[] bytesArchivo = null;
    private String nombreArchivo = "archivo.pdf";
    private String mimeType = "application/pdf";

    private TextView ueri;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.activity_subir_apuntes,
                container,
                false
        );

        ueri = view.findViewById(R.id.ueriApunte);

        Button botonSubir = view.findViewById(R.id.botonSubirApunte);
        Button botonGuardar = view.findViewById(R.id.botonGuardarApuntes);
        TextView inputNombre = view.findViewById(R.id.textInputLayoutnombreApunte);

        // File picker launcher
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == Activity.RESULT_OK &&
                            result.getData() != null) {

                        Uri uri = result.getData().getData();
                        leerArchivoInmediatamente(uri);
                    }
                });

        botonSubir.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        });

        botonGuardar.setOnClickListener(v -> {

            if (bytesArchivo == null) {
                Toast.makeText(getContext(),
                        "Selecciona un archivo",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String nombreParaDjango = inputNombre.getText()
                    .toString().trim();

            if (nombreParaDjango.isEmpty())
                nombreParaDjango = "Apunte_Sin_Nombre";

            subir(nombreParaDjango);
        });

        return view;




    }

    private void leerArchivoInmediatamente(Uri uri) {

        try (InputStream is =
                     requireContext().getContentResolver().openInputStream(uri)) {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            int len;

            while ((len = is.read(buffer)) != -1)
                bos.write(buffer, 0, len);

            bytesArchivo = bos.toByteArray();

            mimeType =
                    requireContext().getContentResolver().getType(uri);

            if (mimeType == null)
                mimeType = "application/octet-stream";

            try (Cursor cursor =
                         requireContext().getContentResolver()
                                 .query(uri, null, null, null, null)) {

                if (cursor != null && cursor.moveToFirst()) {

                    int nameIndex =
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                    if (nameIndex != -1)
                        nombreArchivo = cursor.getString(nameIndex);
                }
            }

            ueri.setText("Archivo cargado: " + bytesArchivo.length + " bytes");

        } catch (Exception e) {
            Log.e(TAG, "Error leyendo archivo", e);
        }
    }

    // ----------------------------
    // Subir archivo
    // ----------------------------

    private void subir(String nombre) {

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext());

        String email = preferences.getString("email", null);

        if (email == null) {
            Toast.makeText(getContext(),
                    "No hay sesión",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = getArguments();

        if (bundle == null || !bundle.containsKey("nombreTarjeta")) {
            Toast.makeText(getContext(),
                    "Debes seleccionar un centro primero",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String nombreTarjeta = bundle.getString("nombreTarjeta");

        RequestBody requestFile =
                RequestBody.create(MediaType.parse(mimeType), bytesArchivo);

        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData(
                        "url",
                        nombreArchivo,
                        requestFile
                );

        RequestBody reqName =
                RequestBody.create(MediaType.parse("text/plain"), nombre);

        RequestBody reqEmail =
                RequestBody.create(MediaType.parse("text/plain"), email);

        RequestBody reqTarjeta =
                RequestBody.create(MediaType.parse("text/plain"), nombreTarjeta);

        ApiInterfaz api =
                ApiCliente.getClient2().create(ApiInterfaz.class);

        Call<ResponseBody> call =
                api.subirApuntes(filePart, reqName, reqEmail, reqTarjeta);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(getContext(),
                            "Archivo subido correctamente",
                            Toast.LENGTH_LONG).show();


                } else {

                    try {
                        Log.e(TAG,
                                response.errorBody().string());
                    } catch (Exception ignored) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Error red", t);
            }
        });
    }
}