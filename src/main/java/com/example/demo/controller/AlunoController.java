package com.example.demo.controller;

import com.example.demo.dto.AlunoRequest;
import com.example.demo.dto.AlunoResponse;
import com.example.demo.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.demo.model.Aluno;
import com.example.demo.repository.AlunoRepository;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {
    
    @Autowired
    private AlunoService alunoService;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @GetMapping("/unidade/{idUnidade}")
    public ResponseEntity<List<AlunoResponse>> listarPorUnidade(@PathVariable Long idUnidade) {
        try {
            List<AlunoResponse> alunos = alunoService.listarPorUnidade(idUnidade);
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/unidade/{idUnidade}/busca")
    public ResponseEntity<List<AlunoResponse>> buscarPorUnidadeETermo(
            @PathVariable Long idUnidade,
            @RequestParam String termo) {
        try {
            List<AlunoResponse> alunos = alunoService.buscarPorUnidadeETermo(idUnidade, termo);
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<AlunoResponse> buscarPorId(
            @PathVariable Long id,
            @PathVariable Long idUnidade) {
        try {
            AlunoResponse aluno = alunoService.buscarPorId(id, idUnidade);
            return ResponseEntity.ok(aluno);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<AlunoResponse> criar(@RequestBody AlunoRequest request) {
        try {
            AlunoResponse aluno = alunoService.criar(request);
            return ResponseEntity.ok(aluno);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<AlunoResponse> atualizar(
            @PathVariable Long id,
            @PathVariable Long idUnidade,
            @RequestBody AlunoRequest request) {
        try {
            AlunoResponse aluno = alunoService.atualizar(id, request, idUnidade);
            return ResponseEntity.ok(aluno);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}/unidade/{idUnidade}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id,
            @PathVariable Long idUnidade) {
        try {
            alunoService.excluir(id, idUnidade);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/teste-valor-mensalidade")
    public ResponseEntity<Map<String, Object>> testarValorMensalidade(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== TESTE VALOR MENSALIDADE ===");
            System.out.println("Request recebido: " + request);
            
            Object valorOriginal = request.get("valor");
            System.out.println("Valor original: '" + valorOriginal + "' (Tipo: " + (valorOriginal != null ? valorOriginal.getClass().getSimpleName() : "null") + ")");
            
            // Simular a conversão que fazemos no service
            BigDecimal valorConvertido = null;
            if (valorOriginal instanceof BigDecimal) {
                valorConvertido = (BigDecimal) valorOriginal;
            } else if (valorOriginal instanceof Number) {
                valorConvertido = new BigDecimal(valorOriginal.toString());
            } else if (valorOriginal instanceof String) {
                String strValor = (String) valorOriginal;
                if (!strValor.trim().isEmpty()) {
                    String valorLimpo = strValor.replace("R$ ", "").replace(".", "").replace(",", ".");
                    if (!valorLimpo.trim().isEmpty() && !valorLimpo.equals("0")) {
                        valorConvertido = new BigDecimal(valorLimpo);
                    }
                }
            }
            
            if (valorConvertido == null) {
                valorConvertido = BigDecimal.ZERO;
            }
            
            System.out.println("Valor convertido: " + valorConvertido + " (Tipo: " + valorConvertido.getClass().getSimpleName() + ")");
            
            // Tentar inserir diretamente no banco
            Aluno alunoTeste = new Aluno();
            alunoTeste.setNome("TESTE VALOR MSG " + System.currentTimeMillis());
            alunoTeste.setCpf("123.456.789-00");
            alunoTeste.setCelular("(11) 99999-9999");
            alunoTeste.setVlrMensalidade(valorConvertido);
            alunoTeste.setStatus(1);
            alunoTeste.setBolsista(false);
            alunoTeste.setIdUnidade(1L);
            alunoTeste.setDataCriacao(LocalDateTime.now());
            alunoTeste.setDataAtualizacao(LocalDateTime.now());
            
            System.out.println("Aluno teste antes de salvar: " + alunoTeste);
            System.out.println("Valor mensalidade antes de salvar: " + alunoTeste.getVlrMensalidade());
            
            Aluno alunoSalvo = alunoRepository.save(alunoTeste);
            
            System.out.println("Aluno salvo com sucesso! ID: " + alunoSalvo.getId());
            System.out.println("Valor mensalidade após salvar: " + alunoSalvo.getVlrMensalidade());
            
            // Buscar do banco para confirmar
            Aluno alunoRecuperado = alunoRepository.findById(alunoSalvo.getId()).orElse(null);
            System.out.println("Aluno recuperado do banco: " + alunoRecuperado);
            System.out.println("Valor mensalidade recuperado: " + (alunoRecuperado != null ? alunoRecuperado.getVlrMensalidade() : "null"));
            
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("sucesso", true);
            resultado.put("valorOriginal", valorOriginal);
            resultado.put("valorConvertido", valorConvertido);
            resultado.put("alunoSalvo", alunoSalvo);
            resultado.put("alunoRecuperado", alunoRecuperado);
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            System.out.println("Erro no teste: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("erro", e.getMessage());
            erro.put("stackTrace", e.getStackTrace());
            
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @GetMapping("/teste-simples")
    public ResponseEntity<Map<String, Object>> testeSimples() {
        try {
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("mensagem", "Backend funcionando!");
            resultado.put("timestamp", System.currentTimeMillis());
            resultado.put("status", "OK");
            
            System.out.println("=== TESTE SIMPLES EXECUTADO ===");
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("mensagem", "Erro no backend");
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}
