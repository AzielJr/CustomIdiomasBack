package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TurmaRequest {
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
    private Integer idCoordenador;
    private Integer idMaterialDidatico;
    private String sala;
    private Integer capacidadeMaxima;
    private Integer numeroAlunos;
    private String observacoes;
    private Boolean ativo;
    private Long idUnidade;
}
