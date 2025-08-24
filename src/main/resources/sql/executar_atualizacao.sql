-- Script para executar todas as atualizações necessárias
-- Execute este script para resolver o problema do campo Material Didático e Valor da Mensalidade

-- 1. Primeiro, verificar a estrutura atual
DESCRIBE alunos;

-- 2. Adicionar a coluna id_material_didatico se não existir
ALTER TABLE alunos ADD COLUMN IF NOT EXISTS id_material_didatico BIGINT;

-- 3. Verificar se a coluna vlr_mensalidade tem o tipo correto
-- Se não tiver, alterar para DECIMAL(10,2)
ALTER TABLE alunos MODIFY COLUMN vlr_mensalidade DECIMAL(10,2);

-- 4. Atualizar os registros existentes mapeando os nomes para IDs
-- Para os dados de exemplo que estão no script alunos.sql
UPDATE alunos SET id_material_didatico = 1 WHERE material_didatico = 'English File Intermediate';
UPDATE alunos SET id_material_didatico = 2 WHERE material_didatico = 'Advanced Grammar in Use';
UPDATE alunos SET id_material_didatico = 3 WHERE material_didatico = 'New English File Elementary';
UPDATE alunos SET id_material_didatico = 4 WHERE material_didatico = 'English File Pre-Intermediate';

-- 5. Para outros materiais que possam existir, criar registros na tabela material_didatico
-- (Execute apenas se necessário)
INSERT IGNORE INTO material_didatico (nome, editora, autor, obs, status, id_unidade, data_criacao, data_atualizacao) VALUES
('English File Intermediate', 'Oxford', 'Christina Latham-Koenig', 'Material intermediário', 1, 1, NOW(), NOW()),
('Advanced Grammar in Use', 'Cambridge', 'Martin Hewings', 'Gramática avançada', 1, 1, NOW(), NOW()),
('New English File Elementary', 'Oxford', 'Clive Oxenden', 'Material básico', 1, 1, NOW(), NOW()),
('English File Pre-Intermediate', 'Oxford', 'Christina Latham-Koenig', 'Material pré-intermediário', 1, 1, NOW(), NOW());

-- 6. Adicionar a chave estrangeira (se não existir)
-- ALTER TABLE alunos ADD CONSTRAINT fk_alunos_material_didatico 
-- FOREIGN KEY (id_material_didatico) REFERENCES material_didatico(id);

-- 7. Verificar se a estrutura está correta
DESCRIBE alunos;

-- 8. Verificar alguns registros para confirmar
SELECT id, nome, id_material_didatico, vlr_mensalidade, status, bolsista
FROM alunos
LIMIT 5;

-- 9. Verificar se há dados na tabela material_didatico
SELECT * FROM material_didatico WHERE id_unidade = 1;



