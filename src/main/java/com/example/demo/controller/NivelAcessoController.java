package com.example.demo.controller;

import com.example.demo.dto.NivelAcessoRequest;
import com.example.demo.model.NivelAcesso;
import com.example.demo.service.NivelAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nivel-acesso")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class NivelAcessoController {
    
    @Autowired
    private NivelAcessoService nivelAcessoService;

    // Listar todos os níveis de acesso
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarTodos() {
        try {
            List<NivelAcesso> niveisAcesso = nivelAcessoService.findAll();
            List<Map<String, Object>> response = niveisAcesso.stream()
                .map(nivel -> {
                    Map<String, Object> nivelMap = new HashMap<>();
                    nivelMap.put("id", nivel.getId());
                    nivelMap.put("grupo", nivel.getGrupo());
                    nivelMap.put("detalhes", nivel.getDetalhes());
                    nivelMap.put("nivelAcesso", nivel.getNivelAcesso());
                    nivelMap.put("ativo", nivel.getAtivo());
                    return nivelMap;
                })
                .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            NivelAcesso nivelAcesso = nivelAcessoService.findById(id);
            if (nivelAcesso != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("id", nivelAcesso.getId());
                response.put("grupo", nivelAcesso.getGrupo());
                response.put("detalhes", nivelAcesso.getDetalhes());
                response.put("nivelAcesso", nivelAcesso.getNivelAcesso());
                response.put("ativo", nivelAcesso.getAtivo());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Criar novo nível de acesso
    @PostMapping
    public ResponseEntity<Map<String, Object>> criarNivelAcesso(@RequestBody NivelAcessoRequest request) {
        try {
            // Validações básicas
            if (request.getGrupo() == null || request.getGrupo().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nome do grupo é obrigatório"));
            }
            
            if (request.getNivelAcesso() == null || request.getNivelAcesso().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nível de acesso é obrigatório"));
            }
            
            // Criar objeto NivelAcesso
            NivelAcesso novoNivelAcesso = NivelAcesso.builder()
                .grupo(request.getGrupo().trim())
                .detalhes(request.getDetalhes() != null ? request.getDetalhes().trim() : "")
                .nivelAcesso(request.getNivelAcesso().trim())
                .ativo(request.isAtivo())
                .build();
            
            NivelAcesso nivelSalvo = nivelAcessoService.register(novoNivelAcesso);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", nivelSalvo.getId());
            response.put("grupo", nivelSalvo.getGrupo());
            response.put("detalhes", nivelSalvo.getDetalhes());
            response.put("nivelAcesso", nivelSalvo.getNivelAcesso());
            response.put("ativo", nivelSalvo.getAtivo());
            response.put("message", "Nível de acesso criado com sucesso");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Atualizar nível de acesso
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarNivelAcesso(
            @PathVariable Long id, 
            @RequestBody NivelAcessoRequest request) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID inválido"));
            }
            
            NivelAcesso nivelExistente = nivelAcessoService.findById(id);
            if (nivelExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Validações básicas
            if (request.getGrupo() == null || request.getGrupo().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nome do grupo é obrigatório"));
            }
            
            if (request.getNivelAcesso() == null || request.getNivelAcesso().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nível de acesso é obrigatório"));
            }
            
            // Atualizar dados
            nivelExistente.setGrupo(request.getGrupo().trim());
            nivelExistente.setDetalhes(request.getDetalhes() != null ? request.getDetalhes().trim() : "");
            nivelExistente.setNivelAcesso(request.getNivelAcesso().trim());
            nivelExistente.setAtivo(request.isAtivo());
            
            NivelAcesso nivelAtualizado = nivelAcessoService.register(nivelExistente);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", nivelAtualizado.getId());
            response.put("grupo", nivelAtualizado.getGrupo());
            response.put("detalhes", nivelAtualizado.getDetalhes());
            response.put("nivelAcesso", nivelAtualizado.getNivelAcesso());
            response.put("ativo", nivelAtualizado.getAtivo());
            response.put("message", "Nível de acesso atualizado com sucesso");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Deletar nível de acesso
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarNivelAcesso(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID inválido"));
            }
            
            NivelAcesso nivelExistente = nivelAcessoService.findById(id);
            if (nivelExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            nivelAcessoService.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Nível de acesso excluído com sucesso");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Alternar status ativo/inativo
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Map<String, Object>> alternarStatus(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID inválido"));
            }
            
            NivelAcesso nivelExistente = nivelAcessoService.findById(id);
            if (nivelExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Alternar status
            boolean novoStatus = !nivelExistente.getAtivo();
            nivelExistente.setAtivo(novoStatus);
            
            NivelAcesso nivelAtualizado = nivelAcessoService.register(nivelExistente);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", nivelAtualizado.getId());
            response.put("ativo", nivelAtualizado.getAtivo());
            response.put("message", "Status alterado com sucesso");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}