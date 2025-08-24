package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDidaticoRequest {
    
    private String nome;
    private String editora;
    private String autor;
    private String obs;
    private String fotoCapa;
    private Integer status;
    private Long idUnidade;
}
