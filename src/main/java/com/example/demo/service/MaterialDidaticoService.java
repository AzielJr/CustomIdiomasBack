package com.example.demo.service;

import com.example.demo.dto.MaterialDidaticoRequest;
import com.example.demo.dto.MaterialDidaticoResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.BusinessException;
import com.example.demo.model.MaterialDidatico;
import com.example.demo.repository.MaterialDidaticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialDidaticoService {
    
    @Autowired
    private MaterialDidaticoRepository materialDidaticoRepository;
    
    public List<MaterialDidaticoResponse> listarPorUnidade(Long idUnidade) {
        List<MaterialDidatico> materiais = materialDidaticoRepository.findByIdUnidadeOrderByNomeAsc(idUnidade);
        return materiais.stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }
    
    public List<MaterialDidaticoResponse> buscarPorUnidadeETermo(Long idUnidade, String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarPorUnidade(idUnidade);
        }
        
        List<MaterialDidatico> materiais = materialDidaticoRepository.findByIdUnidadeAndSearchTerm(idUnidade, termo.trim());
        return materiais.stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }
    
    public MaterialDidaticoResponse buscarPorId(Long id, Long idUnidade) {
        MaterialDidatico material = materialDidaticoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material didático não encontrado com ID: " + id));
        
        if (!material.getIdUnidade().equals(idUnidade)) {
            throw new BusinessException("Material didático não pertence à unidade informada");
        }
        
        return converterParaResponse(material);
    }
    
    @Transactional
    public MaterialDidaticoResponse criar(MaterialDidaticoRequest request, Long idUnidade) {
        validarRequest(request);
        
        MaterialDidatico material = new MaterialDidatico();
        material.setNome(request.getNome().trim());
        material.setEditora(request.getEditora().trim());
        material.setAutor(request.getAutor() != null ? request.getAutor().trim() : null);
        material.setObs(request.getObs() != null ? request.getObs().trim() : null);
        material.setFotoCapa(request.getFotoCapa());
        material.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        material.setIdUnidade(idUnidade);
        
        MaterialDidatico materialSalvo = materialDidaticoRepository.save(material);
        return converterParaResponse(materialSalvo);
    }
    
    @Transactional
    public MaterialDidaticoResponse atualizar(Long id, MaterialDidaticoRequest request, Long idUnidade) {
        MaterialDidatico material = materialDidaticoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material didático não encontrado com ID: " + id));
        
        if (!material.getIdUnidade().equals(idUnidade)) {
            throw new BusinessException("Material didático não pertence à unidade informada");
        }
        
        validarRequest(request);
        
        material.setNome(request.getNome().trim());
        material.setEditora(request.getEditora().trim());
        material.setAutor(request.getAutor() != null ? request.getAutor().trim() : null);
        material.setObs(request.getObs() != null ? request.getObs().trim() : null);
        material.setFotoCapa(request.getFotoCapa());
        material.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        
        MaterialDidatico materialAtualizado = materialDidaticoRepository.save(material);
        return converterParaResponse(materialAtualizado);
    }
    
    @Transactional
    public void excluir(Long id, Long idUnidade) {
        if (!materialDidaticoRepository.existsByIdAndIdUnidade(id, idUnidade)) {
            throw new ResourceNotFoundException("Material didático não encontrado com ID: " + id);
        }
        
        materialDidaticoRepository.deleteById(id);
    }
    
    private void validarRequest(MaterialDidaticoRequest request) {
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new BusinessException("Nome do material é obrigatório");
        }
        
        if (request.getEditora() == null || request.getEditora().trim().isEmpty()) {
            throw new BusinessException("Editora é obrigatória");
        }
        
        if (request.getNome().trim().length() > 255) {
            throw new BusinessException("Nome do material deve ter no máximo 255 caracteres");
        }
        
        if (request.getEditora().trim().length() > 255) {
            throw new BusinessException("Editora deve ter no máximo 255 caracteres");
        }
        
        if (request.getAutor() != null && request.getAutor().trim().length() > 255) {
            throw new BusinessException("Autor deve ter no máximo 255 caracteres");
        }
        
        if (request.getStatus() != null && request.getStatus() != 0 && request.getStatus() != 1) {
            throw new BusinessException("Status deve ser 0 (inativo) ou 1 (ativo)");
        }
    }
    
    private MaterialDidaticoResponse converterParaResponse(MaterialDidatico material) {
        return new MaterialDidaticoResponse(
                material.getId(),
                material.getNome(),
                material.getEditora(),
                material.getAutor(),
                material.getObs(),
                material.getFotoCapa(),
                material.getStatus(),
                material.getIdUnidade(),
                material.getDataCriacao(),
                material.getDataAtualizacao()
        );
    }
}
