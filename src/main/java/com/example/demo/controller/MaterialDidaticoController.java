package com.example.demo.controller;

import com.example.demo.dto.MaterialDidaticoRequest;
import com.example.demo.dto.MaterialDidaticoResponse;
import com.example.demo.service.MaterialDidaticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material-didatico")
@CrossOrigin(origins = "*")
public class MaterialDidaticoController {
    
    @Autowired
    private MaterialDidaticoService materialDidaticoService;
    
    @GetMapping("/unidade/{idUnidade}")
    public ResponseEntity<List<MaterialDidaticoResponse>> listarPorUnidade(@PathVariable Long idUnidade) {
        List<MaterialDidaticoResponse> materiais = materialDidaticoService.listarPorUnidade(idUnidade);
        return ResponseEntity.ok(materiais);
    }
    
    @GetMapping("/unidade/{idUnidade}/busca")
    public ResponseEntity<List<MaterialDidaticoResponse>> buscarPorUnidadeETermo(
            @PathVariable Long idUnidade,
            @RequestParam(required = false) String termo) {
        List<MaterialDidaticoResponse> materiais = materialDidaticoService.buscarPorUnidadeETermo(idUnidade, termo);
        return ResponseEntity.ok(materiais);
    }
    
    @GetMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<MaterialDidaticoResponse> buscarPorId(
            @PathVariable Long id,
            @PathVariable Long idUnidade) {
        MaterialDidaticoResponse material = materialDidaticoService.buscarPorId(id, idUnidade);
        return ResponseEntity.ok(material);
    }
    
    @PostMapping("/unidade/{idUnidade}")
    public ResponseEntity<MaterialDidaticoResponse> criar(
            @RequestBody MaterialDidaticoRequest request,
            @PathVariable Long idUnidade) {
        MaterialDidaticoResponse material = materialDidaticoService.criar(request, idUnidade);
        return ResponseEntity.ok(material);
    }
    
    @PutMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<MaterialDidaticoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody MaterialDidaticoRequest request,
            @PathVariable Long idUnidade) {
        MaterialDidaticoResponse material = materialDidaticoService.atualizar(id, request, idUnidade);
        return ResponseEntity.ok(material);
    }
    
    @DeleteMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @PathVariable Long idUnidade) {
        materialDidaticoService.excluir(id, idUnidade);
        return ResponseEntity.noContent().build();
    }
}
