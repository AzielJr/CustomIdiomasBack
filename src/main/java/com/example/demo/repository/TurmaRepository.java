package com.example.demo.repository;

import com.example.demo.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    
    @Query("SELECT t FROM Turma t WHERE t.ativo = true")
    List<Turma> findAllAtivas();
    
    @Query("SELECT t FROM Turma t WHERE t.ativo = true AND t.idProfessor = :idProfessor")
    List<Turma> findByProfessor(@Param("idProfessor") Integer idProfessor);
    
    @Query("SELECT t FROM Turma t WHERE t.ativo = true AND t.idCoordenador = :idCoordenador")
    List<Turma> findByCoordenador(@Param("idCoordenador") Integer idCoordenador);
    
    @Query("SELECT t FROM Turma t WHERE t.ativo = true AND t.nivel = :nivel")
    List<Turma> findByNivel(@Param("nivel") String nivel);
    
    @Query("SELECT t FROM Turma t WHERE t.ativo = true AND t.ano = :ano")
    List<Turma> findByAno(@Param("ano") String ano);
    
    @Query("SELECT t FROM Turma t WHERE t.ativo = true AND " +
           "(LOWER(t.nomeTurma) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(t.nivel) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(t.sala) LIKE LOWER(CONCAT('%', :termo, '%')))")
    List<Turma> findByTermoPesquisa(@Param("termo") String termo);
    
    @Query("SELECT t FROM Turma t WHERE t.ativo = true AND t.idUnidade = :idUnidade ORDER BY t.nomeTurma ASC")
    List<Turma> findByUnidadeIdOrderByNomeTurmaAsc(@Param("idUnidade") Integer idUnidade);
    
    @Query("SELECT t FROM Turma t WHERE t.ativo = true AND t.idUnidade = :idUnidade AND " +
           "(LOWER(t.nomeTurma) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(t.nivel) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(t.sala) LIKE LOWER(CONCAT('%', :termo, '%'))) " +
           "ORDER BY t.nomeTurma ASC")
    List<Turma> findByUnidadeIdAndTermoOrderByNomeTurmaAsc(@Param("idUnidade") Integer idUnidade, @Param("termo") String termo);
}
