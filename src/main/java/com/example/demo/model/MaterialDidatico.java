package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_didatico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDidatico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;
    
    @Column(name = "editora", nullable = false, length = 255)
    private String editora;
    
    @Column(name = "autor", length = 255)
    private String autor;
    
    @Column(name = "obs", columnDefinition = "TEXT")
    private String obs;
    
    @Column(name = "foto_capa", columnDefinition = "LONGTEXT")
    private String fotoCapa;
    
    @Column(name = "status", nullable = false)
    private Integer status; // 1 = Ativo, 0 = Inativo
    
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
