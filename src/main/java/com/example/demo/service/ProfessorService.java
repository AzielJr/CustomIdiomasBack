package com.example.demo.service;

import com.example.demo.dto.ProfessorRequest;
import com.example.demo.dto.ProfessorResponse;
import com.example.demo.model.Professor;
import com.example.demo.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {
    
    @Autowired
    private ProfessorRepository professorRepository;
    
    public List<ProfessorResponse> listarPorUnidade(Long idUnidade) {
        List<Professor> professores = professorRepository.findByIdUnidadeOrderByNomeAsc(idUnidade);
        return professores.stream()
                   .map(this::convertToResponse)
                   .collect(Collectors.toList());
    }
    
    public List<ProfessorResponse> buscarPorUnidadeETermo(Long idUnidade, String termo) {
        List<Professor> professores = professorRepository.findByIdUnidadeAndSearchTerm(idUnidade, termo);
        return professores.stream()
                   .map(this::convertToResponse)
                   .collect(Collectors.toList());
    }
    
    public ProfessorResponse buscarPorId(Long id, Long idUnidade) {
        Professor professor = professorRepository.findByIdAndIdUnidade(id, idUnidade)
                                   .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        return convertToResponse(professor);
    }
    
    public ProfessorResponse criar(ProfessorRequest request) {
        System.out.println("Iniciando criação do professor...");
        System.out.println("Request recebido: " + request);
        System.out.println("Salário no request: '" + request.getSalario() + "'");
        
        validarRequest(request);
        
        System.out.println("Criando objeto Professor...");
        Professor professor = new Professor();
        professor.setNome(request.getNome());
        professor.setCpf(request.getCpf());
        professor.setRg(request.getRg());
        professor.setDataNascimento(request.getDataNascimento());
        professor.setDataAdmissao(request.getDataAdmissao());
        professor.setExperienciaAnos(request.getExperienciaAnos());
        professor.setQtdAlunos(request.getQtdAlunos());
        professor.setFormacaoAcademica(request.getFormacaoAcademica());
        professor.setEndereco(request.getEndereco());
        professor.setNumero(request.getNumero());
        professor.setComplemento(request.getComplemento());
        professor.setBairro(request.getBairro());
        professor.setCidade(request.getCidade());
        professor.setEstado(request.getEstado());
        professor.setCep(request.getCep());
        professor.setTelefone(request.getTelefone());
        professor.setCelular(request.getCelular());
        professor.setEmail(request.getEmail());
        professor.setFoto(request.getFoto());
        professor.setSalario(request.getSalario() != null ? request.getSalario() : BigDecimal.ZERO);
        professor.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        professor.setCoordenador(request.getCoordenador() != null ? request.getCoordenador() : false);
        professor.setIdUnidade(request.getIdUnidade());
        
        System.out.println("Salvando professor no banco de dados...");
        System.out.println("Salário antes de salvar: " + professor.getSalario());
        System.out.println("Professor a ser salvo: " + professor);
        
        Professor professorSalvo = professorRepository.save(professor);
        System.out.println("Professor salvo com sucesso! ID: " + professorSalvo.getId());
        System.out.println("Salário após salvar: " + professorSalvo.getSalario());
        
        return convertToResponse(professorSalvo);
    }
    
    public ProfessorResponse atualizar(Long id, ProfessorRequest request, Long idUnidade) {
        validarRequest(request);
        
        Professor professor = professorRepository.findByIdAndIdUnidade(id, idUnidade)
                                   .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        
        professor.setNome(request.getNome());
        professor.setCpf(request.getCpf());
        professor.setRg(request.getRg());
        professor.setDataNascimento(request.getDataNascimento());
        professor.setDataAdmissao(request.getDataAdmissao());
        professor.setExperienciaAnos(request.getExperienciaAnos());
        professor.setQtdAlunos(request.getQtdAlunos());
        professor.setFormacaoAcademica(request.getFormacaoAcademica());
        professor.setEndereco(request.getEndereco());
        professor.setNumero(request.getNumero());
        professor.setComplemento(request.getComplemento());
        professor.setBairro(request.getBairro());
        professor.setCidade(request.getCidade());
        professor.setEstado(request.getEstado());
        professor.setCep(request.getCep());
        professor.setTelefone(request.getTelefone());
        professor.setCelular(request.getCelular());
        professor.setEmail(request.getEmail());
        professor.setFoto(request.getFoto());
        professor.setSalario(request.getSalario() != null ? request.getSalario() : BigDecimal.ZERO);
        professor.setStatus(request.getStatus() != null ? request.getStatus() : 1);
        professor.setCoordenador(request.getCoordenador() != null ? request.getCoordenador() : false);
        
        Professor professorAtualizado = professorRepository.save(professor);
        return convertToResponse(professorAtualizado);
    }
    
    public void excluir(Long id, Long idUnidade) {
        Professor professor = professorRepository.findByIdAndIdUnidade(id, idUnidade)
                                   .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        professorRepository.delete(professor);
    }
    
    private void validarRequest(ProfessorRequest request) {
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        if (request.getIdUnidade() == null || request.getIdUnidade() == 0) {
            throw new RuntimeException("Unidade é obrigatória");
        }
    }
    
    private ProfessorResponse convertToResponse(Professor professor) {
        ProfessorResponse response = new ProfessorResponse();
        response.setId(professor.getId());
        response.setNome(professor.getNome());
        response.setCpf(professor.getCpf());
        response.setRg(professor.getRg());
        response.setDataNascimento(professor.getDataNascimento());
        response.setDataAdmissao(professor.getDataAdmissao());
        response.setExperienciaAnos(professor.getExperienciaAnos());
        response.setQtdAlunos(professor.getQtdAlunos());
        response.setFormacaoAcademica(professor.getFormacaoAcademica());
        response.setEndereco(professor.getEndereco());
        response.setNumero(professor.getNumero());
        response.setComplemento(professor.getComplemento());
        response.setBairro(professor.getBairro());
        response.setCidade(professor.getCidade());
        response.setEstado(professor.getEstado());
        response.setCep(professor.getCep());
        response.setTelefone(professor.getTelefone());
        response.setCelular(professor.getCelular());
        response.setEmail(professor.getEmail());
        response.setFoto(professor.getFoto());
        response.setSalario(professor.getSalario());
        response.setStatus(professor.getStatus());
        response.setCoordenador(professor.getCoordenador());
        response.setIdUnidade(professor.getIdUnidade());
        response.setDataCriacao(professor.getDataCriacao());
        response.setDataAtualizacao(professor.getDataAtualizacao());
        return response;
    }
}
