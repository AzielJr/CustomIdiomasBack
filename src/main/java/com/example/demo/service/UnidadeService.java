package com.example.demo.service;

import com.example.demo.model.Unidade;
import com.example.demo.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeService {
    
    private final UnidadeRepository unidadeRepository;
    
    @Autowired
    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }
    
    // Listar todas as unidades
    public List<Unidade> findAll() {
        return unidadeRepository.findAll();
    }
    
    // Buscar unidade por ID
    public Unidade findById(Long id) {
        Optional<Unidade> unidade = unidadeRepository.findById(id);
        return unidade.orElse(null);
    }
    
    // Salvar nova unidade
    public Unidade save(Unidade unidade) {
        return unidadeRepository.save(unidade);
    }
    
    // Atualizar unidade existente
    public Unidade update(Long id, Unidade unidadeAtualizada) {
        Optional<Unidade> unidadeExistente = unidadeRepository.findById(id);
        if (unidadeExistente.isPresent()) {
            Unidade unidade = unidadeExistente.get();
            
            // Atualizar campos
            unidade.setRazaoSocial(unidadeAtualizada.getRazaoSocial());
            unidade.setFantasia(unidadeAtualizada.getFantasia());
            unidade.setCnpj(unidadeAtualizada.getCnpj());
            unidade.setContato(unidadeAtualizada.getContato());
            unidade.setCelular_contato(unidadeAtualizada.getCelular_contato());
            unidade.setLogomarca(unidadeAtualizada.getLogomarca());
            unidade.setEmail(unidadeAtualizada.getEmail());
            unidade.setCep(unidadeAtualizada.getCep());
            unidade.setEndereco(unidadeAtualizada.getEndereco());
            unidade.setNumero(unidadeAtualizada.getNumero());
            unidade.setUf(unidadeAtualizada.getUf());
            unidade.setComplemento(unidadeAtualizada.getComplemento());
            unidade.setBairro(unidadeAtualizada.getBairro());
            unidade.setCidade(unidadeAtualizada.getCidade());
            unidade.setAtivo(unidadeAtualizada.getAtivo());
            
            return unidadeRepository.save(unidade);
        }
        return null;
    }
    
    // Excluir unidade por ID
    public void deleteById(Long id) {
        unidadeRepository.deleteById(id);
    }
    
    // Verificar se CNPJ já existe
    public boolean existsByCnpj(String cnpj) {
        return unidadeRepository.existsByCnpj(cnpj);
    }
    
    // Verificar se CNPJ já existe para outro ID
    public boolean existsByCnpjAndNotId(String cnpj, Long id) {
        return unidadeRepository.existsByCnpjAndNotId(cnpj, id);
    }
    
    // Verificar se email já existe
    public boolean existsByEmail(String email) {
        return unidadeRepository.existsByEmail(email);
    }
    
    // Verificar se email já existe para outro ID
    public boolean existsByEmailAndNotId(String email, Long id) {
        return unidadeRepository.existsByEmailAndNotId(email, id);
    }
    
    // Buscar unidades ativas
    public List<Unidade> findByAtivoTrue() {
        return unidadeRepository.findByAtivoTrue();
    }
    
    // Buscar por razão social
    public List<Unidade> findByRazaoSocialContaining(String razaoSocial) {
        return unidadeRepository.findByRazaoSocialContainingIgnoreCase(razaoSocial);
    }
    
    // Buscar por nome fantasia
    public List<Unidade> findByFantasiaContaining(String fantasia) {
        return unidadeRepository.findByFantasiaContainingIgnoreCase(fantasia);
    }
    
    // Contar unidades ativas
    public long countByAtivoTrue() {
        return unidadeRepository.countByAtivoTrue();
    }
    
    // Contar total de unidades
    public long countTotal() {
        return unidadeRepository.countTotal();
    }
    
    // Buscar por CNPJ
    public Unidade findByCnpj(String cnpj) {
        Optional<Unidade> unidade = unidadeRepository.findByCnpj(cnpj);
        return unidade.orElse(null);
    }
    
    // Buscar por email
    public Unidade findByEmail(String email) {
        Optional<Unidade> unidade = unidadeRepository.findByEmail(email);
        return unidade.orElse(null);
    }
}