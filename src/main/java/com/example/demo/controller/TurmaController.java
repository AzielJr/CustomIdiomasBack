package com.example.demo.controller;

import com.example.demo.dto.TurmaRequest;
import com.example.demo.dto.TurmaResponse;
import com.example.demo.dto.ProfessorResponse;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.dto.MaterialDidaticoResponse;
import com.example.demo.service.TurmaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TurmaController {
    
    private final TurmaService turmaService;
    
    @GetMapping
    public ResponseEntity<List<TurmaResponse>> listarTodas() {
        log.info("GET /api/turmas - Listando todas as turmas");
        List<TurmaResponse> turmas = turmaService.listarTodas();
        return ResponseEntity.ok(turmas);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponse> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/turmas/{} - Buscando turma por ID", id);
        TurmaResponse turma = turmaService.buscarPorId(id);
        return ResponseEntity.ok(turma);
    }
    
    @PostMapping
    public ResponseEntity<TurmaResponse> criar(@RequestBody TurmaRequest request) {
        log.info("POST /api/turmas - Criando nova turma: {}", request.getNomeTurma());
        TurmaResponse turma = turmaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(turma);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TurmaResponse> atualizar(@PathVariable Long id, @RequestBody TurmaRequest request) {
        log.info("PUT /api/turmas/{} - Atualizando turma", id);
        TurmaResponse turma = turmaService.atualizar(id, request);
        return ResponseEntity.ok(turma);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /api/turmas/{} - Deletando turma", id);
        turmaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/pesquisa")
    public ResponseEntity<List<TurmaResponse>> buscarPorTermo(@RequestParam String termo) {
        log.info("GET /api/turmas/pesquisa?termo={} - Pesquisando turmas", termo);
        List<TurmaResponse> turmas = turmaService.buscarPorTermo(termo);
        return ResponseEntity.ok(turmas);
    }
    
    @GetMapping("/unidade/{idUnidade}")
    public ResponseEntity<List<TurmaResponse>> buscarPorUnidade(@PathVariable Long idUnidade) {
        log.info("GET /api/turmas/unidade/{} - Buscando turmas da unidade", idUnidade);
        List<TurmaResponse> turmas = turmaService.buscarPorUnidade(idUnidade);
        return ResponseEntity.ok(turmas);
    }
    
    @GetMapping("/unidade/{idUnidade}/pesquisa")
    public ResponseEntity<List<TurmaResponse>> buscarPorTermoEUnidade(@PathVariable Long idUnidade, @RequestParam String termo) {
        log.info("GET /api/turmas/unidade/{}/pesquisa?termo={} - Pesquisando turmas da unidade", idUnidade, termo);
        List<TurmaResponse> turmas = turmaService.buscarPorTermoEUnidade(termo, idUnidade);
        return ResponseEntity.ok(turmas);
    }
    
    @GetMapping("/professores/{idUnidade}")
    public ResponseEntity<List<ProfessorResponse>> buscarProfessores(@PathVariable Long idUnidade) {
        log.info("GET /api/turmas/professores/{} - Buscando professores da unidade", idUnidade);
        List<ProfessorResponse> professores = turmaService.buscarProfessoresAtivos(idUnidade);
        return ResponseEntity.ok(professores);
    }
    
    @GetMapping("/coordenadores/{idUnidade}")
    public ResponseEntity<List<ProfessorResponse>> buscarCoordenadores(@PathVariable Long idUnidade) {
        log.info("GET /api/turmas/coordenadores/{} - Buscando coordenadores da unidade", idUnidade);
        List<ProfessorResponse> coordenadores = turmaService.buscarCoordenadoresAtivos(idUnidade);
        return ResponseEntity.ok(coordenadores);
    }
    
    @GetMapping("/materiais/{idUnidade}")
    public ResponseEntity<List<MaterialDidaticoResponse>> buscarMateriais(@PathVariable Long idUnidade) {
        log.info("GET /api/turmas/materiais/{} - Buscando materiais da unidade", idUnidade);
        List<MaterialDidaticoResponse> materiais = turmaService.buscarMateriaisAtivos(idUnidade);
        return ResponseEntity.ok(materiais);
    }
}
