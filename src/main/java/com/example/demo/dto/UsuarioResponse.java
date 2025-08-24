package com.example.demo.dto;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioResponse {
    private Long id;
    private String email;
    private String role;
    private String userName;
    private String celular;
    private Long idNivelAcesso;
    private Long idUnidade;
    private Boolean ativo;
    private LocalDateTime ultimoLogin;
    private String nivelAcessoGrupo;
    private String nivelAcessoDetalhes;
    private boolean nivelAcessoAtivo;
    private byte[] foto;
}