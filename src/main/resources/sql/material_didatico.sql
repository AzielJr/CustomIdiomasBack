-- Criação da tabela material_didatico
CREATE TABLE IF NOT EXISTS material_didatico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    editora VARCHAR(255) NOT NULL,
    autor VARCHAR(255),
    obs TEXT,
    foto_capa LONGTEXT,
    status INT NOT NULL DEFAULT 1 COMMENT '1 = Ativo, 0 = Inativo',
    id_unidade BIGINT NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME,
    
    INDEX idx_id_unidade (id_unidade),
    INDEX idx_status (status),
    INDEX idx_nome (nome),
    INDEX idx_editora (editora),
    
    FOREIGN KEY (id_unidade) REFERENCES unidades(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Inserir dados de exemplo (opcional)
INSERT INTO material_didatico (nome, editora, autor, obs, status, id_unidade, data_criacao, data_atualizacao) VALUES
('English Grammar in Use', 'Cambridge University Press', 'Raymond Murphy', 'Livro intermediário de gramática inglesa', 1, 1, NOW(), NOW()),
('New Headway Elementary', 'Oxford University Press', 'John Soars', 'Curso completo para iniciantes', 1, 1, NOW(), NOW()),
('Business English Course', 'Pearson', 'David Cotton', 'Inglês para negócios e ambiente corporativo', 1, 1, NOW(), NOW()),
('Interchange Intro', 'Cambridge', 'Jack C. Richards', 'Material básico para iniciantes', 1, 1, NOW(), NOW());
