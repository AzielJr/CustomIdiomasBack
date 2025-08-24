package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDidaticoResponse {
    
    private Long id;
    private String nome;
    private String editora;
    private String autor;
    private String obs;
    private String fotoCapa;
    private Integer status;
    private Long idUnidade;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
