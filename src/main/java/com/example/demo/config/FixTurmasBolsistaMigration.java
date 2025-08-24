package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
@Order(2)
public class FixTurmasBolsistaMigration implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        log.info("🔧 Iniciando migração para remover campo bolsista da tabela turmas...");
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Verificar se o campo bolsista existe na tabela turmas
            boolean campoExiste = false;
            try (ResultSet columns = metaData.getColumns(null, null, "turmas", "bolsista")) {
                campoExiste = columns.next();
            }
            
            if (campoExiste) {
                log.info("📋 Campo 'bolsista' encontrado na tabela 'turmas'. Removendo...");
                
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                jdbcTemplate.execute("ALTER TABLE turmas DROP COLUMN bolsista");
                log.info("✅ Campo 'bolsista' removido com sucesso da tabela 'turmas'");
            } else {
                log.info("ℹ️ Campo 'bolsista' não encontrado na tabela 'turmas'. Nada a fazer.");
            }
            
        } catch (SQLException e) {
            log.error("❌ Erro ao verificar estrutura da tabela: {}", e.getMessage());
        }
        
        log.info("🏁 Migração para remover campo bolsista da tabela turmas concluída");
    }
}
