package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String email;
    private String userName;
    private String tokenExpiration;
    private byte[] foto;

}
