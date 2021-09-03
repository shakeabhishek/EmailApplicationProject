package com.test.EmailApplicationProject.models;

public class LoginRequest {
    private String Email;
    private String Password;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public LoginRequest() {}

    public LoginRequest(String email, String password) {
        Email = email;
        Password = password;
    }
}
