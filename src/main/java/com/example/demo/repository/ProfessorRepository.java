package com.example.demo.repository;

import com.example.demo.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    // Buscar por unidade
    List<Professor> findByIdUnidadeOrderByNomeAsc(Long idUnidade);
    
    // Buscar por ID e unidade
    Optional<Professor> findByIdAndIdUnidade(Long id, Long idUnidade);
    
    // Buscar por unidade com termo de pesquisa
    @Query("SELECT p FROM Professor p WHERE p.idUnidade = :idUnidade AND " +
           "(LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(p.cpf) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :termo, '%'))) " +
           "ORDER BY p.nome ASC")
    List<Professor> findByIdUnidadeAndSearchTerm(@Param("idUnidade") Long idUnidade, @Param("termo") String termo);
    
    // Buscar professores ativos por unidade (status = 1)
    List<Professor> findByStatusAndIdUnidadeOrderByNomeAsc(Integer status, Long idUnidade);
    
    // Buscar professores coordenadores por unidade
    List<Professor> findByCoordenadorTrueAndIdUnidadeOrderByNomeAsc(Long idUnidade);
}
