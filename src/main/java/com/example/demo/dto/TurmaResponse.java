package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TurmaResponse {
    private Long id;
    private String nomeTurma;
    private String nivel;
    private String ano;
    private String horaInicio;
    private String horaFim;
    private Boolean aulaSeg;
    private Boolean aulaTer;
    private Boolean aulaQua;
    private Boolean aulaQui;
    private Boolean aulaSex;
    private Boolean aulaSab;
    private Integer idProfessor;
    private String professorNome;
    private Integer idCoordenador;
    private String coordenadorNome;
    private Integer idMaterialDidatico;
    private String materialNome;
    private String sala;
    private Integer capacidadeMaxima;
    private Integer numeroAlunos;
    private String observacoes;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
