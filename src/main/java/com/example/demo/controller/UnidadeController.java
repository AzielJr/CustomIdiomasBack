package com.example.demo.controller;

import com.example.demo.model.Unidade;
import com.example.demo.service.UnidadeService;
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
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UnidadeController {
    
    @Autowired
    private UnidadeService unidadeService;
    
    @Autowired
    private ImageUtils imageUtils;
    
    // Debug endpoint para verificar se o controller está funcionando
    @GetMapping("/debug")
    public ResponseEntity<Map<String, Object>> debugUnidades() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "UnidadeController está funcionando");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("endpoint", "/api/unidades/debug");
        System.out.println("[DEBUG] Endpoint de debug acessado com sucesso");
        return ResponseEntity.ok(response);
    }
    
    // Listar todas as unidades
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listarTodas() {
        try {
            System.out.println("[DEBUG] Iniciando listarTodas()");
            List<Unidade> unidades = unidadeService.findAll();
            System.out.println("[DEBUG] Encontradas " + unidades.size() + " unidades");
            
            List<Map<String, Object>> response = unidades.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
            System.out.println("[DEBUG] Conversão concluída, retornando " + response.size() + " unidades");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("[ERROR] Erro em listarTodas(): " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Buscar unidade por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            Unidade unidade = unidadeService.findById(id);
            if (unidade != null) {
                Map<String, Object> response = convertToResponse(unidade);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Erro ao buscar unidade por ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Criar nova unidade
    @PostMapping
    public ResponseEntity<Map<String, Object>> criarUnidade(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("[DEBUG] Criando nova unidade: " + request);
            
            // Validações básicas
            if (!request.containsKey("razaoSocial") || request.get("razaoSocial").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Razão Social é obrigatória"));
            }
            
            if (!request.containsKey("fantasia") || request.get("fantasia").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nome Fantasia é obrigatório"));
            }
            
            if (!request.containsKey("cnpj") || request.get("cnpj").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "CNPJ é obrigatório"));
            }
            
            if (!request.containsKey("email") || request.get("email").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email é obrigatório"));
            }
            
            String cnpj = request.get("cnpj").toString().trim();
            String email = request.get("email").toString().trim();
            
            // Verificar se CNPJ já existe
            if (unidadeService.existsByCnpj(cnpj)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "CNPJ já está em uso"));
            }
            
            // Verificar se email já existe
            if (unidadeService.existsByEmail(email)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email já está em uso"));
            }
            
            // Criar nova unidade
            Unidade novaUnidade = new Unidade();
            novaUnidade.setRazaoSocial(request.get("razaoSocial").toString().trim());
            novaUnidade.setFantasia(request.get("fantasia").toString().trim());
            novaUnidade.setCnpj(cnpj);
            novaUnidade.setEmail(email);
            novaUnidade.setContato(request.getOrDefault("contato", "").toString().trim());
            novaUnidade.setCelular_contato(request.getOrDefault("celular_contato", "").toString().trim());
            novaUnidade.setCep(request.getOrDefault("cep", "").toString().trim());
            novaUnidade.setEndereco(request.getOrDefault("endereco", "").toString().trim());
            novaUnidade.setNumero(request.getOrDefault("numero", "").toString().trim());
            novaUnidade.setUf(request.getOrDefault("uf", "SP").toString().trim());
            novaUnidade.setComplemento(request.getOrDefault("complemento", "").toString().trim());
            novaUnidade.setBairro(request.getOrDefault("bairro", "").toString().trim());
            novaUnidade.setCidade(request.getOrDefault("cidade", "").toString().trim());
            novaUnidade.setAtivo(Boolean.parseBoolean(request.getOrDefault("ativo", "true").toString()));
            novaUnidade.setInstancia(request.getOrDefault("instancia", "").toString().trim());
            novaUnidade.setToken(request.getOrDefault("token", "").toString().trim());
            
            // Processar logomarca se fornecida
            if (request.containsKey("logomarca") && request.get("logomarca") != null) {
                String logomarcaBase64 = request.get("logomarca").toString();
                if (!logomarcaBase64.isEmpty()) {
                    try {
                        byte[] logomarcaBytes = imageUtils.base64ToByteArray(logomarcaBase64);
                        novaUnidade.setLogomarca(logomarcaBytes);
                    } catch (Exception e) {
                        System.err.println("[ERROR] Erro ao processar logomarca: " + e.getMessage());
                        return ResponseEntity.badRequest()
                            .body(Map.of("error", "Erro ao processar imagem da logomarca"));
                    }
                }
            }
            
            Unidade unidadeSalva = unidadeService.save(novaUnidade);
            
            Map<String, Object> response = convertToResponse(unidadeSalva);
            response.put("message", "Unidade criada com sucesso");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            System.err.println("[ERROR] Erro ao criar unidade: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Atualizar unidade
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUnidade(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> request) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID inválido"));
            }
            
            Unidade unidadeExistente = unidadeService.findById(id);
            if (unidadeExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Validações básicas
            if (!request.containsKey("razaoSocial") || request.get("razaoSocial").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Razão Social é obrigatória"));
            }
            
            if (!request.containsKey("fantasia") || request.get("fantasia").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Nome Fantasia é obrigatório"));
            }
            
            if (!request.containsKey("cnpj") || request.get("cnpj").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "CNPJ é obrigatório"));
            }
            
            if (!request.containsKey("email") || request.get("email").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email é obrigatório"));
            }
            
            String cnpj = request.get("cnpj").toString().trim();
            String email = request.get("email").toString().trim();
            
            // Verificar se CNPJ já existe para outro ID
            if (unidadeService.existsByCnpjAndNotId(cnpj, id)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "CNPJ já está em uso por outra unidade"));
            }
            
            // Verificar se email já existe para outro ID
            if (unidadeService.existsByEmailAndNotId(email, id)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email já está em uso por outra unidade"));
            }
            
            // Atualizar campos
            unidadeExistente.setRazaoSocial(request.get("razaoSocial").toString().trim());
            unidadeExistente.setFantasia(request.get("fantasia").toString().trim());
            unidadeExistente.setCnpj(cnpj);
            unidadeExistente.setEmail(email);
            unidadeExistente.setContato(request.getOrDefault("contato", "").toString().trim());
            unidadeExistente.setCelular_contato(request.getOrDefault("celular_contato", "").toString().trim());
            unidadeExistente.setCep(request.getOrDefault("cep", "").toString().trim());
            unidadeExistente.setEndereco(request.getOrDefault("endereco", "").toString().trim());
            unidadeExistente.setNumero(request.getOrDefault("numero", "").toString().trim());
            unidadeExistente.setUf(request.getOrDefault("uf", "SP").toString().trim());
            unidadeExistente.setComplemento(request.getOrDefault("complemento", "").toString().trim());
            unidadeExistente.setBairro(request.getOrDefault("bairro", "").toString().trim());
            unidadeExistente.setCidade(request.getOrDefault("cidade", "").toString().trim());
            unidadeExistente.setAtivo(Boolean.parseBoolean(request.getOrDefault("ativo", "true").toString()));
            unidadeExistente.setInstancia(request.getOrDefault("instancia", "").toString().trim());
            unidadeExistente.setToken(request.getOrDefault("token", "").toString().trim());
            
            // Processar logomarca se fornecida
            if (request.containsKey("logomarca")) {
                Object logomarcaObj = request.get("logomarca");
                if (logomarcaObj != null && !logomarcaObj.toString().isEmpty()) {
                    try {
                        String logomarcaBase64 = logomarcaObj.toString();
                        byte[] logomarcaBytes = imageUtils.base64ToByteArray(logomarcaBase64);
                        unidadeExistente.setLogomarca(logomarcaBytes);
                    } catch (Exception e) {
                        System.err.println("[ERROR] Erro ao processar logomarca: " + e.getMessage());
                        return ResponseEntity.badRequest()
                            .body(Map.of("error", "Erro ao processar imagem da logomarca"));
                    }
                } else {
                    // Se logomarca for null ou vazia, manter a existente ou definir como null
                    // unidadeExistente.setLogomarca(null);
                }
            }
            
            Unidade unidadeAtualizada = unidadeService.save(unidadeExistente);
            
            Map<String, Object> response = convertToResponse(unidadeAtualizada);
            response.put("message", "Unidade atualizada com sucesso");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("[ERROR] Erro ao atualizar unidade: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Deletar unidade
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarUnidade(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID inválido"));
            }
            
            Unidade unidadeExistente = unidadeService.findById(id);
            if (unidadeExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            unidadeService.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Unidade excluída com sucesso");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("[ERROR] Erro ao deletar unidade: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    // Endpoint para estatísticas
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", unidadeService.countTotal());
            stats.put("ativas", unidadeService.countByAtivoTrue());
            stats.put("inativas", unidadeService.countTotal() - unidadeService.countByAtivoTrue());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("[ERROR] Erro ao obter estatísticas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Endpoint para consultar CNPJ na Receita Federal
    @GetMapping("/consultar-cnpj/{cnpj}")
    public ResponseEntity<Map<String, Object>> consultarCNPJ(@PathVariable String cnpj) {
        try {
            // Remover caracteres não numéricos do CNPJ
            String cnpjLimpo = cnpj.replaceAll("\\D", "");
            
            if (cnpjLimpo.length() != 14) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "CNPJ deve ter 14 dígitos"));
            }
            
            // URL da API da Receita Federal
            String url = "https://www.receitaws.com.br/v1/cnpj/" + cnpjLimpo;
            
            // Fazer requisição HTTP
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
            
            java.net.http.HttpResponse<String> response = client.send(request, 
                java.net.http.HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                // Parsear resposta JSON
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                Map<String, Object> dadosReceita = mapper.readValue(response.body(), Map.class);
                
                // Verificar se a consulta foi bem-sucedida
                if ("OK".equals(dadosReceita.get("status"))) {
                    Map<String, Object> resultado = new HashMap<>();
                    resultado.put("nome", dadosReceita.get("nome"));
                    resultado.put("fantasia", dadosReceita.get("fantasia"));
                    resultado.put("logradouro", dadosReceita.get("logradouro"));
                    resultado.put("numero", dadosReceita.get("numero"));
                    resultado.put("cep", dadosReceita.get("cep"));
                    resultado.put("uf", dadosReceita.get("uf"));
                    resultado.put("bairro", dadosReceita.get("bairro"));
                    resultado.put("municipio", dadosReceita.get("municipio"));
                    resultado.put("complemento", dadosReceita.get("complemento"));
                    resultado.put("email", dadosReceita.get("email"));
                    
                    // Extrair primeiro sócio-administrador
                    if (dadosReceita.containsKey("qsa") && dadosReceita.get("qsa") instanceof java.util.List) {
                        java.util.List<?> qsa = (java.util.List<?>) dadosReceita.get("qsa");
                        if (!qsa.isEmpty() && qsa.get(0) instanceof java.util.Map) {
                            java.util.Map<?, ?> primeiroSocio = (java.util.Map<?, ?>) qsa.get(0);
                            resultado.put("contato", primeiroSocio.get("nome"));
                        }
                    }
                    
                    resultado.put("message", "CNPJ consultado com sucesso");
                    return ResponseEntity.ok(resultado);
                } else {
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "CNPJ não encontrado na Receita Federal"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao consultar Receita Federal: " + response.statusCode()));
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Erro ao consultar CNPJ: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro interno ao consultar CNPJ: " + e.getMessage()));
        }
    }
    
    // Método auxiliar para converter Unidade para Map
    private Map<String, Object> convertToResponse(Unidade unidade) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", unidade.getId());
        response.put("razaoSocial", unidade.getRazaoSocial());
        response.put("fantasia", unidade.getFantasia());
        response.put("cnpj", unidade.getCnpj());
        response.put("contato", unidade.getContato());
        response.put("celular_contato", unidade.getCelular_contato());
        response.put("email", unidade.getEmail());
        response.put("cep", unidade.getCep());
        response.put("endereco", unidade.getEndereco());
        response.put("numero", unidade.getNumero());
        response.put("uf", unidade.getUf());
        response.put("complemento", unidade.getComplemento());
        response.put("bairro", unidade.getBairro());
        response.put("cidade", unidade.getCidade());
        response.put("ativo", unidade.getAtivo());
        response.put("instancia", unidade.getInstancia());
        response.put("token", unidade.getToken());
        
        // Converter logomarca para base64 se existir
        if (unidade.getLogomarca() != null && unidade.getLogomarca().length > 0) {
            try {
                String logomarcaBase64 = imageUtils.byteArrayToBase64(unidade.getLogomarca(), "image/jpeg");
                response.put("logomarca", logomarcaBase64);
            } catch (Exception e) {
                System.err.println("[ERROR] Erro ao converter logomarca para base64: " + e.getMessage());
                response.put("logomarca", null);
            }
        } else {
            response.put("logomarca", null);
        }
        
        return response;
    }
}