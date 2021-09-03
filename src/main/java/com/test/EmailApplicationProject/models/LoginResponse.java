package com.test.EmailApplicationProject.models;

public class LoginResponse {
    public String jwt;

    public LoginResponse(String jwt) {
        this.jwt = jwt;
    }

    public LoginResponse() {
    }
}
