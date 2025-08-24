package com.example.demo.repository;

import com.example.demo.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    List<Aluno> findByIdUnidadeOrderByNomeAsc(Long idUnidade);
    
    @Query("SELECT a FROM Aluno a WHERE a.idUnidade = :idUnidade AND " +
           "(LOWER(a.nome) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.cpf) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Aluno> findByIdUnidadeAndSearchTerm(@Param("idUnidade") Long idUnidade, 
                                            @Param("searchTerm") String searchTerm);
    
    boolean existsByIdAndIdUnidade(Long id, Long idUnidade);
    
    Optional<Aluno> findByIdAndIdUnidade(Long id, Long idUnidade);
}
