package com.example.demo.controller;

import com.example.demo.dto.UsuarioRequest;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.model.NivelAcesso;
import com.example.demo.model.Usuario;
import com.example.demo.service.NivelAcessoService;
import com.example.demo.service.UsuarioService;
import com.example.demo.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NivelAcessoService nivelAcessoService;
    
    @Autowired
    private ImageUtils imageUtils;

    // Debug endpoint para verificar se o controller está sendo acessado
    @GetMapping("/debug")
    public ResponseEntity<Map<String, Object>> debugUsuarios() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "UsuarioController está funcionando");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("endpoint", "/api/usuarios/debug");
        System.out.println("[DEBUG] Endpoint de debug acessado com sucesso");
        return ResponseEntity.ok(response);
    }
    

    
    // Listar todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        try {
            System.out.println("[DEBUG] Iniciando listarTodos()");
            List<Usuario> usuarios = usuarioService.findAll();
            System.out.println("[DEBUG] Encontrados " + usuarios.size() + " usuários");
            
            List<UsuarioResponse> response = usuarios.stream()
                .map(this::convertToResponseSafe)
                .collect(Collectors.toList());
            System.out.println("[DEBUG] Conversão concluída, retornando " + response.size() + " usuários");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("[ERROR] Erro em listarTodos(): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            Usuario usuario = usuarioService.findById(id);
            if (usuario != null) {
                UsuarioResponse response = convertToResponse(usuario);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Criar novo usuário
    @PostMapping
    public ResponseEntity<Map<String, Object>> criarUsuario(@RequestBody UsuarioRequest request) {
        try {
            // Validações básicas
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email é obrigatório"));
            }
            
            if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nome de usuário é obrigatório"));
            }

            if (request.getSenha() == null || request.getSenha().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Senha é obrigatória"));
            }
            
            // Verificar se email já existe
            if (usuarioService.existsByEmail(request.getEmail().trim())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email já está em uso"));
            }
            
            // Processar foto se fornecida
            byte[] fotoBytes = null;
            if (request.getFoto() != null && !request.getFoto().trim().isEmpty()) {
                try {
                    if (!imageUtils.isValidBase64Image(request.getFoto())) {
                        return ResponseEntity.badRequest()
                            .body(Map.of("error", "Formato de imagem inválido. Use uma imagem válida em base64."));
                    }
                    fotoBytes = imageUtils.base64ToByteArray(request.getFoto());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Erro ao processar imagem: " + e.getMessage()));
                }
            }
            
            // Criar objeto Usuario
            Usuario novoUsuario = Usuario.builder()
                .email(request.getEmail().trim())
                .userName(request.getUserName().trim())
                .senha(request.getSenha())
                .role(request.getRole())
                .foto(fotoBytes)
                .celular(request.getCelular())
                .idNivelAcesso(request.getIdNivelAcesso())
                .idUnidade(request.getIdUnidade())
                .ativo(request.getAtivo() != null ? request.getAtivo() : true)
                .build();
            
            Usuario usuarioSalvo = usuarioService.register(novoUsuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", usuarioSalvo.getId());
            response.put("email", usuarioSalvo.getEmail());
            response.put("userName", usuarioSalvo.getUserName());
            response.put("role", usuarioSalvo.getRole());
            response.put("celular", usuarioSalvo.getCelular());
            response.put("idNivelAcesso", usuarioSalvo.getIdNivelAcesso());
            response.put("idUnidade", usuarioSalvo.getIdUnidade());
            response.put("ativo", usuarioSalvo.getAtivo());
            response.put("message", "Usuário criado com sucesso");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            System.err.println("[ERROR] Erro ao criar usuário: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(
            @PathVariable Long id, 
            @RequestBody UsuarioRequest request) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID inválido"));
            }
            
            Usuario usuarioExistente = usuarioService.findById(id);
            if (usuarioExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Validações básicas
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email é obrigatório"));
            }
            
            if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nome de usuário é obrigatório"));
            }
            
            // Verificar se email já existe para outro usuário
            if (usuarioService.existsByEmailAndNotId(request.getEmail().trim(), id)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email já está em uso por outro usuário"));
            }
            
            // Processar foto se fornecida
            if (request.getFoto() != null && !request.getFoto().trim().isEmpty()) {
                try {
                    if (!imageUtils.isValidBase64Image(request.getFoto())) {
                        return ResponseEntity.badRequest()
                            .body(Map.of("error", "Formato de imagem inválido. Use uma imagem válida em base64."));
                    }
                    byte[] fotoBytes = imageUtils.base64ToByteArray(request.getFoto());
                    usuarioExistente.setFoto(fotoBytes);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Erro ao processar imagem: " + e.getMessage()));
                }
            }
            
            // Atualizar dados
            usuarioExistente.setEmail(request.getEmail().trim());
            usuarioExistente.setUserName(request.getUserName().trim());
            usuarioExistente.setRole(request.getRole());
            usuarioExistente.setCelular(request.getCelular());
            usuarioExistente.setIdNivelAcesso(request.getIdNivelAcesso());
            usuarioExistente.setIdUnidade(request.getIdUnidade());
            usuarioExistente.setAtivo(request.getAtivo() != null ? request.getAtivo() : usuarioExistente.getAtivo());
            
            // Só atualizar senha se foi fornecida
            if (request.getSenha() != null && !request.getSenha().trim().isEmpty()) {
                usuarioExistente.setSenha(request.getSenha());
            }
            
            Usuario usuarioAtualizado = usuarioService.update(usuarioExistente);
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", usuarioAtualizado.getId());
            response.put("email", usuarioAtualizado.getEmail());
            response.put("userName", usuarioAtualizado.getUserName());
            response.put("role", usuarioAtualizado.getRole());
            response.put("celular", usuarioAtualizado.getCelular());
            response.put("idNivelAcesso", usuarioAtualizado.getIdNivelAcesso());
            response.put("idUnidade", usuarioAtualizado.getIdUnidade());
            response.put("ativo", usuarioAtualizado.getAtivo());
            response.put("message", "Usuário atualizado com sucesso");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarUsuario(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID inválido"));
            }
            
            Usuario usuarioExistente = usuarioService.findById(id);
            if (usuarioExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            usuarioService.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuário excluído com sucesso");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Upload de foto do usuário
    @PostMapping("/{id}/foto")
    public ResponseEntity<Map<String, Object>> uploadFoto(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String fotoBase64 = request.get("foto");
            
            if (fotoBase64 == null || fotoBase64.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Foto é obrigatória"));
            }
            
            // Validar formato da imagem
            if (!imageUtils.isValidBase64Image(fotoBase64)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Formato de imagem inválido. Use uma imagem válida em base64."));
            }
            
            // Converter para byte array
            byte[] fotoBytes = imageUtils.base64ToByteArray(fotoBase64);
            
            // Buscar usuário
             Usuario usuario = usuarioService.findById(id);
             if (usuario == null) {
                 return ResponseEntity.notFound().build();
             }
             
             // Atualizar foto
             usuario.setFoto(fotoBytes);
             Usuario usuarioAtualizado = usuarioService.update(usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Foto atualizada com sucesso");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Erro ao processar imagem: " + e.getMessage()));
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Método auxiliar para converter Usuario em UsuarioResponse
    private UsuarioResponse convertToResponseSafe(Usuario usuario) {
        return UsuarioResponse.builder()
            .id(usuario.getId())
            .email(usuario.getEmail())
            .role(usuario.getRole())
            .userName(usuario.getUserName())
            .celular(usuario.getCelular())
            .idNivelAcesso(usuario.getIdNivelAcesso())
            .idUnidade(usuario.getIdUnidade())
            .ativo(usuario.getAtivo())
            .ultimoLogin(usuario.getUltimoLogin())
            .foto(usuario.getFoto())
            .nivelAcessoGrupo(null)
            .nivelAcessoDetalhes(null)
            .nivelAcessoAtivo(false)
            .build();
    }
    
    private UsuarioResponse convertToResponse(Usuario usuario) {
        UsuarioResponse.UsuarioResponseBuilder builder = UsuarioResponse.builder()
            .id(usuario.getId())
            .email(usuario.getEmail())
            .role(usuario.getRole())
            .userName(usuario.getUserName())
            .celular(usuario.getCelular())
            .idNivelAcesso(usuario.getIdNivelAcesso())
            .idUnidade(usuario.getIdUnidade())
            .ativo(usuario.getAtivo())
            .ultimoLogin(usuario.getUltimoLogin())
            .foto(usuario.getFoto());
        
        // Buscar informações do nível de acesso se existir
        if (usuario.getIdNivelAcesso() != null) {
            try {
                NivelAcesso nivelAcesso = nivelAcessoService.findById(usuario.getIdNivelAcesso());
                if (nivelAcesso != null) {
                    builder.nivelAcessoGrupo(nivelAcesso.getGrupo())
                           .nivelAcessoDetalhes(nivelAcesso.getDetalhes())
                           .nivelAcessoAtivo(Boolean.TRUE.equals(nivelAcesso.getAtivo()));
                }
            } catch (Exception e) {
                // Log do erro mas não interrompe a conversão
                System.err.println("Erro ao buscar nível de acesso para usuário " + usuario.getId() + ": " + e.getMessage());
            }
        }
        
        return builder.build();
    }
}