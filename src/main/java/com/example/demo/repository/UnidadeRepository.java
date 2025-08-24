package com.example.demo.repository;

import com.example.demo.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
    
    // Buscar por CNPJ
    Optional<Unidade> findByCnpj(String cnpj);
    
    // Verificar se CNPJ já existe
    boolean existsByCnpj(String cnpj);
    
    // Verificar se CNPJ já existe para outro ID
    @Query("SELECT COUNT(u) > 0 FROM Unidade u WHERE u.cnpj = :cnpj AND u.id != :id")
    boolean existsByCnpjAndNotId(@Param("cnpj") String cnpj, @Param("id") Long id);
    
    // Buscar por email
    Optional<Unidade> findByEmail(String email);
    
    // Verificar se email já existe
    boolean existsByEmail(String email);
    
    // Verificar se email já existe para outro ID
    @Query("SELECT COUNT(u) > 0 FROM Unidade u WHERE u.email = :email AND u.id != :id")
    boolean existsByEmailAndNotId(@Param("email") String email, @Param("id") Long id);
    
    // Buscar unidades ativas
    List<Unidade> findByAtivoTrue();
    
    // Buscar por razão social (case insensitive)
    @Query("SELECT u FROM Unidade u WHERE LOWER(u.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))")
    List<Unidade> findByRazaoSocialContainingIgnoreCase(@Param("razaoSocial") String razaoSocial);
    
    // Buscar por nome fantasia (case insensitive)
    @Query("SELECT u FROM Unidade u WHERE LOWER(u.fantasia) LIKE LOWER(CONCAT('%', :fantasia, '%'))")
    List<Unidade> findByFantasiaContainingIgnoreCase(@Param("fantasia") String fantasia);
    
    // Contar unidades ativas
    @Query("SELECT COUNT(u) FROM Unidade u WHERE u.ativo = true")
    long countByAtivoTrue();
    
    // Contar total de unidades
    @Query("SELECT COUNT(u) FROM Unidade u")
    long countTotal();
}
