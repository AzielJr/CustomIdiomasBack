package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AlunoResponse {
    private Long id;
    private String nome;
    private String cpf;
    private String rg;
    private String dataNascimento;
    private String nivelEnsino;
    private Long idMaterialDidatico;
    private String filiacaoPai;
    private String filiacaoMae;
    private String responsavel;
    private String responsavelCelular;
    private String emergenciaLigarPara;
    private String emergenciaLevarPara;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    private String celular;
    private String email;
    private String foto;
    private BigDecimal vlrMensalidade;
    private Integer status;
    private Boolean bolsista;
    private Long idUnidade;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
