package com.example.demo.service;

import com.example.demo.dto.TurmaRequest;
import com.example.demo.dto.TurmaResponse;
import com.example.demo.dto.ProfessorResponse;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.dto.MaterialDidaticoResponse;
import com.example.demo.model.Turma;
import com.example.demo.repository.TurmaRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.MaterialDidaticoRepository;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TurmaService {
    
    private final TurmaRepository turmaRepository;
    private final ProfessorRepository professorRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialDidaticoRepository materialDidaticoRepository;
    
    public List<TurmaResponse> listarTodas() {
        log.info("Listando todas as turmas ativas");
        return turmaRepository.findAllAtivas()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public TurmaResponse buscarPorId(Long id) {
        log.info("Buscando turma com ID: {}", id);
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com ID: " + id));
        return convertToResponse(turma);
    }
    
    @Transactional
    public TurmaResponse criar(TurmaRequest request) {
        log.info("Criando nova turma: {}", request.getNomeTurma());
        
        try {
            validarRequest(request);
            
            Turma turma = new Turma();
            mapearRequestParaEntity(request, turma);
            turma.setDataCriacao(LocalDateTime.now());
            turma.setDataAtualizacao(LocalDateTime.now());
            turma.setAtivo(true);
            
            log.info("Turma antes de salvar: {}", turma);
            
            Turma turmaSalva = turmaRepository.save(turma);
            log.info("Turma criada com sucesso. ID: {}", turmaSalva.getId());
            
            return convertToResponse(turmaSalva);
        } catch (Exception e) {
            log.error("Erro ao criar turma: {}", e.getMessage(), e);
            throw new BusinessException("Erro ao criar turma: " + e.getMessage());
        }
    }
    
    @Transactional
    public TurmaResponse atualizar(Long id, TurmaRequest request) {
        log.info("Atualizando turma com ID: {}", id);
        validarRequest(request);
        
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com ID: " + id));
        
        mapearRequestParaEntity(request, turma);
        turma.setDataAtualizacao(LocalDateTime.now());
        
        Turma turmaAtualizada = turmaRepository.save(turma);
        log.info("Turma atualizada com sucesso. ID: {}", turmaAtualizada.getId());
        
        return convertToResponse(turmaAtualizada);
    }
    
    @Transactional
    public void deletar(Long id) {
        log.info("Deletando turma com ID: {}", id);
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Turma não encontrada com ID: " + id));
        
        turma.setAtivo(false);
        turma.setDataAtualizacao(LocalDateTime.now());
        turmaRepository.save(turma);
        
        log.info("Turma deletada com sucesso. ID: {}", id);
    }
    
    public List<TurmaResponse> buscarPorTermo(String termo) {
        log.info("Buscando turmas com termo: {}", termo);
        return turmaRepository.findByTermoPesquisa(termo)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Métodos para buscar dados relacionados
    public List<ProfessorResponse> buscarProfessoresAtivos(Long idUnidade) {
        log.info("Buscando professores ativos da unidade: {}", idUnidade);
        return professorRepository.findByStatusAndIdUnidadeOrderByNomeAsc(1, idUnidade)
                .stream()
                .map(professor -> {
                    ProfessorResponse response = new ProfessorResponse();
                    response.setId(professor.getId());
                    response.setNome(professor.getNome());
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    public List<ProfessorResponse> buscarCoordenadoresAtivos(Long idUnidade) {
        log.info("Buscando coordenadores ativos da unidade: {}", idUnidade);
        return professorRepository.findByCoordenadorTrueAndIdUnidadeOrderByNomeAsc(idUnidade)
                .stream()
                .map(professor -> {
                    ProfessorResponse response = new ProfessorResponse();
                    response.setId(professor.getId());
                    response.setNome(professor.getNome());
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    public List<MaterialDidaticoResponse> buscarMateriaisAtivos(Long idUnidade) {
        log.info("Buscando materiais didáticos ativos da unidade: {}", idUnidade);
        return materialDidaticoRepository.findByStatusAndIdUnidadeOrderByNomeAsc(1, idUnidade)
                .stream()
                .map(material -> {
                    MaterialDidaticoResponse response = new MaterialDidaticoResponse();
                    response.setId(material.getId());
                    response.setNome(material.getNome());
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    public List<TurmaResponse> buscarPorUnidade(Long idUnidade) {
        log.info("Buscando turmas da unidade: {}", idUnidade);
        return turmaRepository.findByUnidadeIdOrderByNomeTurmaAsc(idUnidade.intValue())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<TurmaResponse> buscarPorTermoEUnidade(String termo, Long idUnidade) {
        log.info("Buscando turmas da unidade {} com termo: {}", idUnidade, termo);
        return turmaRepository.findByUnidadeIdAndTermoOrderByNomeTurmaAsc(idUnidade.intValue(), termo)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private void validarRequest(TurmaRequest request) {
        if (request.getNomeTurma() == null || request.getNomeTurma().trim().isEmpty()) {
            throw new BusinessException("Nome da turma é obrigatório");
        }
        
        if (request.getNivel() == null || request.getNivel().trim().isEmpty()) {
            throw new BusinessException("Nível é obrigatório");
        }
        
        if (request.getAno() == null || request.getAno().trim().isEmpty()) {
            throw new BusinessException("Ano é obrigatório");
        }
        
        if (request.getHoraInicio() == null || request.getHoraInicio().trim().isEmpty()) {
            throw new BusinessException("Horário de início é obrigatório");
        }
        
        if (request.getHoraFim() == null || request.getHoraFim().trim().isEmpty()) {
            throw new BusinessException("Horário de fim é obrigatório");
        }
        
        if (request.getSala() == null || request.getSala().trim().isEmpty()) {
            throw new BusinessException("Sala é obrigatória");
        }
        
        if (request.getCapacidadeMaxima() == null || request.getCapacidadeMaxima() <= 0) {
            throw new BusinessException("Capacidade máxima deve ser maior que zero");
        }
        
        if (request.getIdUnidade() == null) {
            throw new BusinessException("Unidade é obrigatória");
        }
    }
    
    private void mapearRequestParaEntity(TurmaRequest request, Turma turma) {
        try {
            turma.setNomeTurma(request.getNomeTurma());
            turma.setNivel(request.getNivel());
            turma.setAno(request.getAno());
            turma.setHoraInicio(request.getHoraInicio());
            turma.setHoraFim(request.getHoraFim());
            turma.setAulaSeg(request.getAulaSeg() != null ? request.getAulaSeg() : false);
            turma.setAulaTer(request.getAulaTer() != null ? request.getAulaTer() : false);
            turma.setAulaQua(request.getAulaQua() != null ? request.getAulaQua() : false);
            turma.setAulaQui(request.getAulaQui() != null ? request.getAulaQui() : false);
            turma.setAulaSex(request.getAulaSex() != null ? request.getAulaSex() : false);
            turma.setAulaSab(request.getAulaSab() != null ? request.getAulaSab() : false);
            turma.setIdProfessor(request.getIdProfessor());
            turma.setIdCoordenador(request.getIdCoordenador());
            turma.setIdMaterialDidatico(request.getIdMaterialDidatico());
            turma.setSala(request.getSala());
            turma.setCapacidadeMaxima(request.getCapacidadeMaxima());
            turma.setNumeroAlunos(request.getNumeroAlunos() != null ? request.getNumeroAlunos() : 0);
            turma.setObservacoes(request.getObservacoes());
            turma.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
            
            // Definir idUnidade
            if (request.getIdUnidade() != null) {
                turma.setIdUnidade(request.getIdUnidade().intValue());
                log.info("Definindo idUnidade para turma: {}", request.getIdUnidade());
            } else {
                log.warn("idUnidade é null no request");
            }
            
            log.info("Mapeamento concluído com sucesso");
        } catch (Exception e) {
            log.error("Erro no mapeamento: {}", e.getMessage(), e);
            throw new BusinessException("Erro no mapeamento dos dados: " + e.getMessage());
        }
    }
    
    private TurmaResponse convertToResponse(Turma turma) {
        TurmaResponse response = new TurmaResponse();
        response.setId(turma.getId());
        response.setNomeTurma(turma.getNomeTurma());
        response.setNivel(turma.getNivel());
        response.setAno(turma.getAno());
        response.setHoraInicio(turma.getHoraInicio());
        response.setHoraFim(turma.getHoraFim());
        response.setAulaSeg(turma.getAulaSeg());
        response.setAulaTer(turma.getAulaTer());
        response.setAulaQua(turma.getAulaQua());
        response.setAulaQui(turma.getAulaQui());
        response.setAulaSex(turma.getAulaSex());
        response.setAulaSab(turma.getAulaSab());
        response.setIdProfessor(turma.getIdProfessor());
        response.setIdCoordenador(turma.getIdCoordenador());
        response.setIdMaterialDidatico(turma.getIdMaterialDidatico());
        response.setSala(turma.getSala());
        response.setCapacidadeMaxima(turma.getCapacidadeMaxima());
        response.setNumeroAlunos(turma.getNumeroAlunos());
        response.setObservacoes(turma.getObservacoes());
        response.setAtivo(turma.getAtivo());
        response.setDataCriacao(turma.getDataCriacao());
        response.setDataAtualizacao(turma.getDataAtualizacao());
        
        // Buscar nomes relacionados
        if (turma.getIdProfessor() != null) {
            professorRepository.findById(turma.getIdProfessor().longValue())
                    .ifPresent(professor -> response.setProfessorNome(professor.getNome()));
        }
        
        if (turma.getIdCoordenador() != null) {
            professorRepository.findById(turma.getIdCoordenador().longValue())
                    .ifPresent(professor -> response.setCoordenadorNome(professor.getNome()));
        }
        
        if (turma.getIdMaterialDidatico() != null) {
            materialDidaticoRepository.findById(turma.getIdMaterialDidatico().longValue())
                    .ifPresent(material -> response.setMaterialNome(material.getNome()));
        }
        
        return response;
    }
}
