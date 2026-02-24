package com.example.studybro.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studybro.FormUtils;
import com.example.studybro.R;
import com.example.studybro.models.UsuarioRegistro;
import com.example.studybro.apis.ApiCliente;
import com.example.studybro.apis.ApiInterfaz;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextInputLayout correoRegister = findViewById(R.id.emailRegister);
        TextInputLayout direction = findViewById(R.id.Direccion);
        TextInputLayout password = findViewById(R.id.contraseñaREgister);
        TextInputLayout confirmPassword = findViewById(R.id.ConfirmarcontraseñaREgister);
        TextInputLayout telefono = findViewById(R.id.NumTeléfono);
        Button registerButton = findViewById(R.id.RegisterButton);
        TextView accederLogin = findViewById(R.id.HipervinculoRegistrarse);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        FormUtils formUtils = new FormUtils();

        accederLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(Register.this, Login.class);
                startActivity(intentRegister);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean continuar = true;

                String correoElectronico = correoRegister.getEditText().getText().toString();
                String Telefono = telefono.getEditText().getText().toString();
                String direccion = direction.getEditText().getText().toString();
                String contraseña = password.getEditText().getText().toString();
                String confirmarContraseña = confirmPassword.getEditText().getText().toString();


                if (!comprobarDireccion(direccion)) {
                    continuar = false;
                }

                if (!comprobarMail(correoElectronico)) {
                    continuar = false;
                }

                if (!comprobarPasswords(contraseña, confirmarContraseña)) {
                    continuar = false;
                }

                if (!comprobarNumTelefono(Telefono)) {
                    continuar = false;
                }

                if (continuar) {
                    String password = FormUtils.getTilText(confirmPassword);
                    String hashedPassword = FormUtils.generateHashedPassword(password);
                    editor.putString("password", hashedPassword);
                    editor.apply();



                    /// Registrar Usuario///

                    UsuarioRegistro usuarioRegistro = new UsuarioRegistro(correoElectronico, contraseña, confirmarContraseña);
                    ApiInterfaz api = ApiCliente.getClient2().create(ApiInterfaz.class);
                    //El response Body es para enviar una respuesta al servidor
                    Call<ResponseBody> call = api.registrarUsuario(usuarioRegistro);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                            if (response.isSuccessful()) {
                                Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                                Intent intentMain = new Intent(Register.this, Login.class);
                                startActivity(intentMain);
                            } else {


                                try {
                                    String error = response.errorBody().string();
                                    error = error.replace("{\"success\":false,\"errors\":[\"", "");
                                    error = error.replace("\"}", "");
                                    Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            Toast.makeText(Register.this, "Ha habido un error  en el registro", Toast.LENGTH_SHORT).show();
                        }
                    });




                }

            }
        });
    }

    public boolean comprobarDireccion(String direccion) {
        boolean contieneDigitos = false;
        boolean contieneLetras = false;
        boolean contieneAmbos = false;

        for (int i = 0; i < direccion.length(); i++) {
            if (Character.isDigit(direccion.charAt(i))) {
                contieneDigitos = true;
            } else if (Character.isLetter(direccion.charAt(i))) {
                contieneLetras = true;
            }

        }

        if (contieneDigitos && contieneLetras) {
            contieneAmbos = true;
        }
        boolean direccionCorrecta = true;

        if (direccion.isEmpty()) {
            direccionCorrecta = false;
            Toast.makeText(Register.this, "La dirección no puede estar vacía", Toast.LENGTH_SHORT).show();
        } else if (direccion.isBlank()) {
            direccionCorrecta = false;
            Toast.makeText(Register.this, "La dirección no puede estar en blanco", Toast.LENGTH_SHORT).show();
        } else {
            if (!contieneAmbos) {
                direccionCorrecta = false;
                Toast.makeText(Register.this, "La dirección no puede contener solo números o solo letras", Toast.LENGTH_SHORT).show();
            }
        }

        return direccionCorrecta;
    }

    public boolean comprobarMail(String mail) {


        boolean email = true;

        if (mail.isEmpty()) {
            email = false;
            Toast.makeText(Register.this, "El correo electrónico no puede estar vacío", Toast.LENGTH_SHORT).show();
        } else if (mail.isBlank()){
            email = false;

            Toast.makeText(Register.this, "El correo electrónico no puede estar en blanco", Toast.LENGTH_SHORT).show();
        }


        if(email){


            if (!mail.contains("@")) {
                email = false;
                Toast.makeText(Register.this, "El correo electrónico no contiene un @", Toast.LENGTH_SHORT).show();
            }
        }

        return email;
    }

    public boolean comprobarPasswords(String contraseña, String confirmarContraseña) {
        boolean contraseñaCorrecta = true;

        if (contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            contraseñaCorrecta = false;
            Toast.makeText(Register.this, "Ninguna de las contraseñas pueden estar vacías", Toast.LENGTH_SHORT).show();
        } else if (contraseña.isBlank() || confirmarContraseña.isBlank()) {
            contraseñaCorrecta = false;
            Toast.makeText(Register.this, "Ninguna de las contraseñas pueden estar en blanco", Toast.LENGTH_SHORT).show();
        } else {
            if (!contraseña.equals(confirmarContraseña)) {
                contraseñaCorrecta = false;
            }
        }

        return contraseñaCorrecta;
    }

    public boolean comprobarNumTelefono(String telefono) {
        boolean telefonoCorrecto = true;

        for (int i = 0; i < telefono.length(); i++) {
            if (!Character.isDigit(telefono.charAt(i))) {
                telefonoCorrecto = false;
                Toast.makeText(Register.this, "El número de teléfono no puede contener letras", Toast.LENGTH_SHORT).show();
            }
        }

        if (telefono.length() != 9) {
            telefonoCorrecto = false;
            Toast.makeText(Register.this, "El número de teléfono debe tener 9 dígitos", Toast.LENGTH_SHORT).show();
        }

        return telefonoCorrecto;
    }



}
