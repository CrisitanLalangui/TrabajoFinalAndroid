package com.example.studybro;

public class UsuarioRegistro {
    String email;
    String password1;
    String password2;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public UsuarioRegistro(String email, String password1, String password2) {
        this.email = email;
        this.password1 = password1;
        this.password2 = password2;
    }
}
