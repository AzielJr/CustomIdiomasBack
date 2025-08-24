package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    
    @JsonProperty("senha")
    private String senha;
    
    // Aceitar também 'password' do frontend
    @JsonProperty("password")
    public void setPassword(String password) {
        this.senha = password;
    }
}
