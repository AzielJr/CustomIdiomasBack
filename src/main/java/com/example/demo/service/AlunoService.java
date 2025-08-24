package com.example.demo.service;

import com.example.demo.dto.AlunoRequest;
import com.example.demo.dto.AlunoResponse;
import com.example.demo.model.Aluno;
import com.example.demo.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    public List<AlunoResponse> listarPorUnidade(Long idUnidade) {
        List<Aluno> alunos = alunoRepository.findByIdUnidadeOrderByNomeAsc(idUnidade);
        return alunos.stream()
                   .map(this::convertToResponse)
                   .collect(Collectors.toList());
    }
    
    public List<AlunoResponse> buscarPorUnidadeETermo(Long idUnidade, String termo) {
        List<Aluno> alunos = alunoRepository.findByIdUnidadeAndSearchTerm(idUnidade, termo);
        return alunos.stream()
                   .map(this::convertToResponse)
                   .collect(Collectors.toList());
    }
    
    public AlunoResponse buscarPorId(Long id, Long idUnidade) {
        Aluno aluno = alunoRepository.findByIdAndIdUnidade(id, idUnidade)
                                   .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return convertToResponse(aluno);
    }
    
    public AlunoResponse criar(AlunoRequest request) {
        System.out.println("Iniciando criação do aluno...");
        System.out.println("Request recebido: " + request);
        System.out.println("Valor mensalidade no request: '" + request.getVlrMensalidade() + "'");
        System.out.println("Tipo do valor mensalidade no request: " + (request.getVlrMensalidade() != null ? request.getVlrMensalidade().getClass().getSimpleName() : "null"));
        validarRequest(request);
        
        System.out.println("Criando objeto Aluno...");
        Aluno aluno = new Aluno();
        aluno.setNome(request.getNome());
        aluno.setCpf(request.getCpf());
        aluno.setRg(request.getRg());
        aluno.setDataNascimento(request.getDataNascimento());
        aluno.setNivelEnsino(request.getNivelEnsino());
        aluno.setIdMaterialDidatico(request.getIdMaterialDidatico());
        aluno.setFiliacaoPai(request.getFiliacaoPai());
        aluno.setFiliacaoMae(request.getFiliacaoMae());
        aluno.setResponsavel(request.getResponsavel());
        aluno.setResponsavelCelular(request.getResponsavelCelular());
        aluno.setEmergenciaLigarPara(request.getEmergenciaLigarPara());
        aluno.setEmergenciaLevarPara(request.getEmergenciaLevarPara());
        aluno.setEndereco(request.getEndereco());
        aluno.setNumero(request.getNumero());
        aluno.setComplemento(request.getComplemento());
        aluno.setBairro(request.getBairro());
        aluno.setCidade(request.getCidade());
        aluno.setEstado(request.getEstado());
        aluno.setCep(request.getCep());
        aluno.setTelefone(request.getTelefone());
        aluno.setCelular(request.getCelular());
        aluno.setEmail(request.getEmail());
        aluno.setFoto(request.getFoto());
        aluno.setVlrMensalidade(request.getVlrMensalidade() != null ? request.getVlrMensalidade() : BigDecimal.ZERO);
        aluno.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        aluno.setBolsista(request.getBolsista() != null ? request.getBolsista() : false);
        aluno.setIdUnidade(request.getIdUnidade());
        
        System.out.println("Salvando aluno no banco de dados...");
        System.out.println("Valor mensalidade antes de salvar: " + aluno.getVlrMensalidade());
        System.out.println("Tipo do valor mensalidade: " + (aluno.getVlrMensalidade() != null ? aluno.getVlrMensalidade().getClass().getSimpleName() : "null"));
        System.out.println("Aluno a ser salvo: " + aluno);
        
        Aluno alunoSalvo = alunoRepository.save(aluno);
        System.out.println("Aluno salvo com sucesso! ID: " + alunoSalvo.getId());
        System.out.println("Valor mensalidade após salvar: " + alunoSalvo.getVlrMensalidade());
        System.out.println("Tipo do valor mensalidade após salvar: " + (alunoSalvo.getVlrMensalidade() != null ? alunoSalvo.getVlrMensalidade().getClass().getSimpleName() : "null"));
        
        return convertToResponse(alunoSalvo);
    }
    
    public AlunoResponse atualizar(Long id, AlunoRequest request, Long idUnidade) {
        validarRequest(request);
        
        Aluno aluno = alunoRepository.findByIdAndIdUnidade(id, idUnidade)
                                   .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        aluno.setNome(request.getNome());
        aluno.setCpf(request.getCpf());
        aluno.setRg(request.getRg());
        aluno.setDataNascimento(request.getDataNascimento());
        aluno.setNivelEnsino(request.getNivelEnsino());
        aluno.setIdMaterialDidatico(request.getIdMaterialDidatico());
        aluno.setFiliacaoPai(request.getFiliacaoPai());
        aluno.setFiliacaoMae(request.getFiliacaoMae());
        aluno.setResponsavel(request.getResponsavel());
        aluno.setResponsavelCelular(request.getResponsavelCelular());
        aluno.setEmergenciaLigarPara(request.getEmergenciaLigarPara());
        aluno.setEmergenciaLevarPara(request.getEmergenciaLevarPara());
        aluno.setEndereco(request.getEndereco());
        aluno.setNumero(request.getNumero());
        aluno.setComplemento(request.getComplemento());
        aluno.setBairro(request.getBairro());
        aluno.setCidade(request.getCidade());
        aluno.setEstado(request.getEstado());
        aluno.setCep(request.getCep());
        aluno.setTelefone(request.getTelefone());
        aluno.setCelular(request.getCelular());
        aluno.setEmail(request.getEmail());
        aluno.setFoto(request.getFoto());
        aluno.setVlrMensalidade(request.getVlrMensalidade() != null ? request.getVlrMensalidade() : BigDecimal.ZERO);
        aluno.setStatus(request.getStatus());
        aluno.setBolsista(request.getBolsista() != null ? request.getBolsista() : false);
        
        Aluno alunoAtualizado = alunoRepository.save(aluno);
        return convertToResponse(alunoAtualizado);
    }
    
    public void excluir(Long id, Long idUnidade) {
        if (!alunoRepository.existsByIdAndIdUnidade(id, idUnidade)) {
            throw new RuntimeException("Aluno não encontrado");
        }
        alunoRepository.deleteById(id);
    }
    
    private void validarRequest(AlunoRequest request) {
        System.out.println("Validando request: " + request);
        
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        if (request.getIdUnidade() == null) {
            throw new RuntimeException("ID da unidade é obrigatório");
        }
        
        if (request.getNome().length() > 255) {
            throw new RuntimeException("Nome deve ter no máximo 255 caracteres");
        }
        if (request.getCpf() != null && request.getCpf().length() > 14) {
            throw new RuntimeException("CPF deve ter no máximo 14 caracteres");
        }
        if (request.getEmail() != null && request.getEmail().length() > 255) {
            throw new RuntimeException("Email deve ter no máximo 255 caracteres");
        }
        
        System.out.println("Validação passou com sucesso");
    }
    
    private AlunoResponse convertToResponse(Aluno aluno) {
        AlunoResponse response = new AlunoResponse();
        response.setId(aluno.getId());
        response.setNome(aluno.getNome());
        response.setCpf(aluno.getCpf());
        response.setRg(aluno.getRg());
        response.setDataNascimento(aluno.getDataNascimento());
        response.setNivelEnsino(aluno.getNivelEnsino());
        response.setIdMaterialDidatico(aluno.getIdMaterialDidatico());
        response.setFiliacaoPai(aluno.getFiliacaoPai());
        response.setFiliacaoMae(aluno.getFiliacaoMae());
        response.setResponsavel(aluno.getResponsavel());
        response.setResponsavelCelular(aluno.getResponsavelCelular());
        response.setEmergenciaLigarPara(aluno.getEmergenciaLigarPara());
        response.setEmergenciaLevarPara(aluno.getEmergenciaLevarPara());
        response.setEndereco(aluno.getEndereco());
        response.setNumero(aluno.getNumero());
        response.setComplemento(aluno.getComplemento());
        response.setBairro(aluno.getBairro());
        response.setCidade(aluno.getCidade());
        response.setEstado(aluno.getEstado());
        response.setCep(aluno.getCep());
        response.setTelefone(aluno.getTelefone());
        response.setCelular(aluno.getCelular());
        response.setEmail(aluno.getEmail());
        response.setFoto(aluno.getFoto());
        System.out.println("Convertendo para response - Valor mensalidade: " + aluno.getVlrMensalidade());
        System.out.println("Convertendo para response - Tipo: " + (aluno.getVlrMensalidade() != null ? aluno.getVlrMensalidade().getClass().getSimpleName() : "null"));
        response.setVlrMensalidade(aluno.getVlrMensalidade());
        response.setStatus(aluno.getStatus());
        response.setBolsista(aluno.getBolsista());
        response.setIdUnidade(aluno.getIdUnidade());
        response.setDataCriacao(aluno.getDataCriacao());
        response.setDataAtualizacao(aluno.getDataAtualizacao());
        return response;
    }
    

    
    // Método de conversão removido - agora o DTO aceita BigDecimal diretamente
}
