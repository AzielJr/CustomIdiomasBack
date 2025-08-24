package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="unidades")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String razaoSocial;

    @Column(nullable = false, length = 100)
    private String fantasia;

    @Column(nullable = false, length = 20)
    private String cnpj;

    @Column(length = 60)
    private String contato;

    @Column(length = 20)
    private String celular_contato;

    @Column(length = 20)
    private String celular;

    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] logomarca;

    @Column(length = 80)
    private String email;

    @Column(length = 10)
    private String cep;

    @Column(length = 100)
    private String endereco;

    @Column(length = 10)
    private String numero;

    @Column(length = 2)
    private String uf;

    @Column(length = 100)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String cidade;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    @Column(length = 60)
    private String instancia;
    
    @Column(length = 60)
    private String token;
}