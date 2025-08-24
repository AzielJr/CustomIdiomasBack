package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(2)
@Slf4j
public class FixVlrMensalidadeMigration implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando migração para corrigir coluna vlr_mensalidade...");
        
        try {
            // Verificar se a coluna existe e seu tipo
            String checkColumnSql = """
                SELECT COLUMN_NAME, DATA_TYPE, NUMERIC_PRECISION, NUMERIC_SCALE
                FROM INFORMATION_SCHEMA.COLUMNS 
                WHERE TABLE_SCHEMA = 'customidiomas' 
                AND TABLE_NAME = 'alunos' 
                AND COLUMN_NAME = 'vlr_mensalidade'
                """;
            
            var result = jdbcTemplate.queryForList(checkColumnSql);
            
            if (result.isEmpty()) {
                log.info("Coluna vlr_mensalidade não existe. Criando...");
                createColumn();
            } else {
                var columnInfo = result.get(0);
                String dataType = (String) columnInfo.get("DATA_TYPE");
                Integer precision = (Integer) columnInfo.get("NUMERIC_PRECISION");
                Integer scale = (Integer) columnInfo.get("NUMERIC_SCALE");
                
                log.info("Coluna vlr_mensalidade existe: tipo={}, precisão={}, escala={}", 
                        dataType, precision, scale);
                
                // Se não for DECIMAL(10,2), corrigir
                if (!"DECIMAL".equals(dataType) || precision == null || precision != 10 || scale == null || scale != 2) {
                    log.info("Corrigindo tipo da coluna vlr_mensalidade para DECIMAL(10,2)...");
                    fixColumnType();
                } else {
                    log.info("Coluna vlr_mensalidade já está com o tipo correto DECIMAL(10,2)");
                }
            }
            
            // Testar inserção de um valor decimal
            testDecimalInsertion();
            
            log.info("Migração da coluna vlr_mensalidade concluída com sucesso!");
            
        } catch (Exception e) {
            log.error("Erro durante migração da coluna vlr_mensalidade: {}", e.getMessage(), e);
        }
    }
    
    private void createColumn() {
        String createColumnSql = """
            ALTER TABLE alunos 
            ADD COLUMN vlr_mensalidade DECIMAL(10,2) NULL 
            COMMENT 'Valor da mensalidade com 2 casas decimais'
            """;
        
        jdbcTemplate.execute(createColumnSql);
        log.info("Coluna vlr_mensalidade criada com sucesso");
    }
    
    private void fixColumnType() {
        String fixColumnSql = """
            ALTER TABLE alunos 
            MODIFY COLUMN vlr_mensalidade DECIMAL(10,2) NULL 
            COMMENT 'Valor da mensalidade com 2 casas decimais'
            """;
        
        jdbcTemplate.execute(fixColumnSql);
        log.info("Tipo da coluna vlr_mensalidade corrigido para DECIMAL(10,2)");
    }
    
    private void testDecimalInsertion() {
        try {
            // Verificar se há pelo menos uma unidade para o teste
            String checkUnidadeSql = "SELECT COUNT(*) FROM unidades";
            int countUnidades = jdbcTemplate.queryForObject(checkUnidadeSql, Integer.class);
            
            if (countUnidades > 0) {
                // Inserir um aluno de teste com valor decimal
                String insertTestSql = """
                    INSERT INTO alunos (nome, id_unidade, data_criacao, vlr_mensalidade, status, bolsista) 
                    VALUES (?, ?, NOW(), ?, 1, false)
                    """;
                
                jdbcTemplate.update(insertTestSql, 
                    "Teste Valor Mensalidade", 
                    1L, 
                    150.75
                );
                
                log.info("Teste de inserção com valor decimal realizado com sucesso");
                
                // Verificar se foi inserido corretamente
                String selectTestSql = """
                    SELECT id, nome, vlr_mensalidade FROM alunos 
                    WHERE nome = 'Teste Valor Mensalidade'
                    """;
                
                var testResult = jdbcTemplate.queryForList(selectTestSql);
                if (!testResult.isEmpty()) {
                    var aluno = testResult.get(0);
                    log.info("Aluno de teste inserido: id={}, nome={}, vlr_mensalidade={}", 
                            aluno.get("id"), aluno.get("nome"), aluno.get("vlr_mensalidade"));
                    
                    // Limpar o teste
                    String deleteTestSql = "DELETE FROM alunos WHERE nome = 'Teste Valor Mensalidade'";
                    jdbcTemplate.execute(deleteTestSql);
                    log.info("Aluno de teste removido");
                }
            } else {
                log.warn("Não há unidades cadastradas para realizar teste de inserção");
            }
            
        } catch (Exception e) {
            log.error("Erro durante teste de inserção decimal: {}", e.getMessage());
        }
    }
}
