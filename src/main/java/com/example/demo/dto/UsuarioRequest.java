package com.example.demo.dto;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String email;
    private String role;
    private String senha;
    private String userName;
    private String foto; // Base64 encoded image
    private String celular;
    private Long idNivelAcesso;
    private Long idUnidade;
    private Boolean ativo = true;
}