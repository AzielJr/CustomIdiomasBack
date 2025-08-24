package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AlunoRequest {
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
    private BigDecimal vlrMensalidade; // Mudado para BigDecimal direto
    private Integer status;
    private Boolean bolsista;
    private Long idUnidade;
}
