package com.example.demo.dto;

import lombok.Data;

@Data
public class NivelAcessoRequest {
    private String grupo;
    private String detalhes;
    private String nivelAcesso;
    private boolean ativo;
}
