package com.example.demo.controller;

import com.example.demo.dto.ProfessorRequest;
import com.example.demo.dto.ProfessorResponse;
import com.example.demo.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/professores")
@CrossOrigin(origins = "*")
public class ProfessorController {
    
    @Autowired
    private ProfessorService professorService;
    
    @GetMapping("/unidade/{idUnidade}")
    public ResponseEntity<List<ProfessorResponse>> listarPorUnidade(@PathVariable Long idUnidade) {
        try {
            List<ProfessorResponse> professores = professorService.listarPorUnidade(idUnidade);
            return ResponseEntity.ok(professores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/unidade/{idUnidade}/busca")
    public ResponseEntity<List<ProfessorResponse>> buscarPorUnidadeETermo(
            @PathVariable Long idUnidade, 
            @RequestParam String termo) {
        try {
            List<ProfessorResponse> professores = professorService.buscarPorUnidadeETermo(idUnidade, termo);
            return ResponseEntity.ok(professores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<ProfessorResponse> buscarPorId(
            @PathVariable Long id, 
            @PathVariable Long idUnidade) {
        try {
            ProfessorResponse professor = professorService.buscarPorId(id, idUnidade);
            return ResponseEntity.ok(professor);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ProfessorResponse> criar(@RequestBody ProfessorRequest request) {
        try {
            ProfessorResponse professor = professorService.criar(request);
            return ResponseEntity.ok(professor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<ProfessorResponse> atualizar(
            @PathVariable Long id, 
            @RequestBody ProfessorRequest request, 
            @PathVariable Long idUnidade) {
        try {
            ProfessorResponse professor = professorService.atualizar(id, request, idUnidade);
            return ResponseEntity.ok(professor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id, 
            @PathVariable Long idUnidade) {
        try {
            professorService.excluir(id, idUnidade);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
