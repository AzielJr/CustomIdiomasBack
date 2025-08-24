package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProfessorRequest {
    private String nome;
    private String cpf;
    private String rg;
    private String dataNascimento;
    private String dataAdmissao;
    private Integer experienciaAnos;
    private Integer qtdAlunos;
    private String formacaoAcademica;
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
    private BigDecimal salario;
    private Integer status;
    private Boolean coordenador;
    private Long idUnidade;
}
