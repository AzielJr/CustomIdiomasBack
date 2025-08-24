-- Criação da tabela alunos
CREATE TABLE IF NOT EXISTS alunos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14),
    rg VARCHAR(20),
    data_nascimento VARCHAR(10),
    nivel_ensino VARCHAR(50),
    material_didatico VARCHAR(255),
    filiacao_pai VARCHAR(255),
    filiacao_mae VARCHAR(255),
    responsavel VARCHAR(255),
    responsavel_celular VARCHAR(20),
    emergencia_ligar_para VARCHAR(255),
    emergencia_levar_para VARCHAR(255),
    endereco VARCHAR(500),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(9),
    telefone VARCHAR(20),
    celular VARCHAR(20),
    email VARCHAR(255),
    foto LONGTEXT,
    vlr_mensalidade DECIMAL(10,2),
    status INT NOT NULL DEFAULT 1,
    bolsista BOOLEAN NOT NULL DEFAULT FALSE,
    id_unidade BIGINT NOT NULL,
    data_criacao DATETIME NOT NULL,
    data_atualizacao DATETIME,
    FOREIGN KEY (id_unidade) REFERENCES unidades(id)
);

-- Inserção de dados de exemplo
INSERT INTO alunos (nome, cpf, rg, data_nascimento, nivel_ensino, material_didatico, filiacao_pai, filiacao_mae, responsavel, responsavel_celular, emergencia_ligar_para, emergencia_levar_para, endereco, bairro, cidade, estado, cep, telefone, celular, email, vlr_mensalidade, status, bolsista, id_unidade, data_criacao) VALUES
('João Silva', '123.456.789-00', '12.345.678-9', '15/03/2005', 'Intermediário', 'English File Intermediate', 'Carlos Silva', 'Maria Silva', 'Carlos Silva', '(11) 99999-9999', 'Carlos Silva', 'Carlos Silva', 'Rua das Flores, 123', 'Centro', 'São Paulo', 'SP', '01234-567', '(11) 3333-3333', '(11) 99999-9999', 'joao.silva@email.com', 150.00, 1, FALSE, 1, NOW()),
('Maria Santos', '987.654.321-00', '98.765.432-1', '22/07/2003', 'Avançado', 'Advanced Grammar in Use', 'Pedro Santos', 'Ana Santos', 'Pedro Santos', '(11) 88888-8888', 'Pedro Santos', 'Pedro Santos', 'Av. Paulista, 456', 'Bela Vista', 'São Paulo', 'SP', '01310-000', '(11) 4444-4444', '(11) 88888-8888', 'maria.santos@email.com', 180.00, 1, TRUE, 1, NOW()),
('Pedro Oliveira', '456.789.123-00', '45.678.912-3', '10/11/2006', 'Básico', 'New English File Elementary', 'Roberto Oliveira', 'Claudia Oliveira', 'Roberto Oliveira', '(11) 77777-7777', 'Roberto Oliveira', 'Roberto Oliveira', 'Rua Augusta, 789', 'Consolação', 'São Paulo', 'SP', '01305-000', '(11) 5555-5555', '(11) 77777-7777', 'pedro.oliveira@email.com', 120.00, 1, FALSE, 1, NOW()),
('Ana Costa', '789.123.456-00', '78.912.345-6', '05/01/2004', 'Intermediário', 'English File Pre-Intermediate', 'Fernando Costa', 'Patricia Costa', 'Fernando Costa', '(11) 66666-6666', 'Fernando Costa', 'Fernando Costa', 'Rua Oscar Freire, 321', 'Jardins', 'São Paulo', 'SP', '01426-000', '(11) 6666-6666', '(11) 66666-6666', 'ana.costa@email.com', 160.00, 1, TRUE, 1, NOW());
