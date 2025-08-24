package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="nivel_acesso")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelAcesso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String grupo;

    @Column()
    private String detalhes;

    @Column(nullable = false, length = 30)
    private String nivelAcesso;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}