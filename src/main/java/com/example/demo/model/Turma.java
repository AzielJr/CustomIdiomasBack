package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "turmas")
@Data
public class Turma {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome_turma", nullable = false, length = 60)
    private String nomeTurma;
    
    @Column(name = "nivel", length = 60)
    private String nivel;
    
    @Column(name = "ano", length = 4)
    private String ano;
    
    @Column(name = "hora_inicio", length = 5)
    private String horaInicio;
    
    @Column(name = "hora_fim", length = 5)
    private String horaFim;
    
    @Column(name = "aula_seg")
    private Boolean aulaSeg;
    
    @Column(name = "aula_ter")
    private Boolean aulaTer;
    
    @Column(name = "aula_qua")
    private Boolean aulaQua;
    
    @Column(name = "aula_qui")
    private Boolean aulaQui;
    
    @Column(name = "aula_sex")
    private Boolean aulaSex;
    
    @Column(name = "aula_sab")
    private Boolean aulaSab;
    
    @Column(name = "id_professor")
    private Integer idProfessor;
    
    @Column(name = "id_coordenador")
    private Integer idCoordenador;
    
    @Column(name = "id_material_didatico")
    private Integer idMaterialDidatico;
    
    @Column(name = "id_unidade")
    private Integer idUnidade;
    
    @Column(name = "sala", length = 30)
    private String sala;
    
    @Column(name = "capacidade_maxima")
    private Integer capacidadeMaxima;
    
    @Column(name = "numero_alunos")
    private Integer NumeroAlunos;
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
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
