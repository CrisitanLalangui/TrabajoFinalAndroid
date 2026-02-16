package com.example.studybro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SubirApuntes extends AppCompatActivity {

    private static final int PICK_PDF_FILE = 1;

    TextView ueri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subir_apuntes);

        ueri = findViewById(R.id.ueriApunte);


        Uri pickerInitialUri = Uri.parse(
                "content://com.android.externalstorage.documents/document/primary:Download"
        );

        Button botonGuardarApuntes = findViewById(R.id.botonGuardarApuntes);

        Button botonSubirApunte = findViewById(R.id.botonSubirApunte);
        botonSubirApunte.setOnClickListener(v -> {
            openFile(pickerInitialUri);


        });

        botonGuardarApuntes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SubirApuntes.this, MainActivity.class);
                startActivity(intent);


            }
        });
    }


    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");


        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_PDF_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_PDF_FILE &&
                resultCode == RESULT_OK &&
                data != null) {

            Uri uri = data.getData();

            if (uri != null) {
                ueri.setText(uri.toString());
            }
        }
    }


}
