package com.example.demo.repository;

import com.example.demo.model.MaterialDidatico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialDidaticoRepository extends JpaRepository<MaterialDidatico, Long> {
    
    List<MaterialDidatico> findByIdUnidadeOrderByNomeAsc(Long idUnidade);
    
    @Query("SELECT m FROM MaterialDidatico m WHERE m.idUnidade = :idUnidade AND " +
           "(LOWER(m.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(m.editora) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(m.autor) LIKE LOWER(CONCAT('%', :termo, '%'))) " +
           "ORDER BY m.nome ASC")
    List<MaterialDidatico> findByIdUnidadeAndSearchTerm(@Param("idUnidade") Long idUnidade, @Param("termo") String termo);
    
    boolean existsByIdAndIdUnidade(Long id, Long idUnidade);
    
    // Buscar materiais ativos por unidade (status = 1)
    List<MaterialDidatico> findByStatusAndIdUnidadeOrderByNomeAsc(Integer status, Long idUnidade);
}
