package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "professores")
@Data
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;
    
    @Column(name = "experiencia_anos")
    private Integer experienciaAnos;
    
    @Column(name = "qtd_alunos")
    private Integer qtdAlunos;
    
    @Column(name = "cpf", length = 14)
    private String cpf;
    
    @Column(name = "rg", length = 20)
    private String rg;
    
    @Column(name = "data_nascimento")
    private String dataNascimento;
    
    @Column(name = "data_admissao")
    private String dataAdmissao;
    
    @Column(name = "formacao_academica", columnDefinition = "TEXT")
    private String formacaoAcademica;
        
    @Column(name = "endereco", length = 500)
    private String endereco;
    
    @Column(name = "numero", length = 20)
    private String numero;
    
    @Column(name = "complemento", length = 100)
    private String complemento;
    
    @Column(name = "bairro", length = 100)
    private String bairro;
    
    @Column(name = "cidade", length = 100)
    private String cidade;
    
    @Column(name = "estado", length = 2)
    private String estado;
    
    @Column(name = "cep", length = 9)
    private String cep;
    
    @Column(name = "telefone", length = 20)
    private String telefone;
    
    @Column(name = "celular", length = 20)
    private String celular;
    
    @Column(name = "email", length = 255)
    private String email;
    
    @Column(name = "foto", columnDefinition = "LONGTEXT")
    private String foto;
    
    @Column(name = "salario", precision = 10, scale = 2)
    private BigDecimal salario;
    
    @Column(name = "status", nullable = false)
    private Integer status = 1;
    
    @Column(name = "coordenador", nullable = false)
    private Boolean coordenador = false;
    
    @Column(name = "id_unidade", nullable = false)
    private Long idUnidade;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
