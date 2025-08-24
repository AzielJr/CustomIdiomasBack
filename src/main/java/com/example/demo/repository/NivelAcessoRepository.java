package com.example.demo.repository;

import com.example.demo.model.NivelAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelAcessoRepository extends JpaRepository<NivelAcesso, Long> {
  //  Optional<NivelAcesso> findByEmail(String grupo);
}
