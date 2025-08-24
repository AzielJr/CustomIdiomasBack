package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name="usuarios")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "role")
    private String role;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    @Column(name = "foto", columnDefinition = "LONGBLOB")
    private byte[] foto;

    @Column(name = "celular", length = 15)
    private String celular;

    @Column(name = "id_nivel_acesso")
    private Long idNivelAcesso;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nivel_acesso", insertable = false, updatable = false)
    private NivelAcesso nivelAcesso;
    
    @Column(name = "id_unidade")
    private Long idUnidade;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidade", insertable = false, updatable = false)
    private Unidade unidade;
}
