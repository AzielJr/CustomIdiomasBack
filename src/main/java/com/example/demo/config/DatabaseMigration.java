package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigration implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Verificar se a coluna foto já é LONGBLOB
            String checkColumnQuery = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_SCHEMA = 'customidiomas' AND TABLE_NAME = 'usuarios' AND COLUMN_NAME = 'foto'";
            
            String currentType = jdbcTemplate.queryForObject(checkColumnQuery, String.class);
            
            if (!"LONGBLOB".equalsIgnoreCase(currentType)) {
                System.out.println("Alterando coluna foto de " + currentType + " para LONGBLOB...");
                
                // Alterar coluna para LONGBLOB
                String alterQuery = "ALTER TABLE usuarios MODIFY COLUMN foto LONGBLOB";
                jdbcTemplate.execute(alterQuery);
                
                System.out.println("Coluna foto alterada com sucesso para LONGBLOB!");
            } else {
                System.out.println("Coluna foto já é LONGBLOB, nenhuma alteração necessária.");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao verificar/alterar coluna foto: " + e.getMessage());
            // Não interromper a inicialização da aplicação
        }
    }
}