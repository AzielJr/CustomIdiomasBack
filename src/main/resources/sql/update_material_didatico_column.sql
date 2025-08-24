-- Script completo para atualizar a estrutura da tabela alunos
-- Execute este script para resolver o problema do campo Material Didático

-- 1. Primeiro, adiciona a nova coluna id_material_didatico
ALTER TABLE alunos ADD COLUMN id_material_didatico BIGINT;

-- 2. Atualiza os registros existentes mapeando os nomes para IDs
-- Para os dados de exemplo que estão no script alunos.sql
UPDATE alunos SET id_material_didatico = 1 WHERE material_didatico = 'English File Intermediate';
UPDATE alunos SET id_material_didatico = 2 WHERE material_didatico = 'Advanced Grammar in Use';
UPDATE alunos SET id_material_didatico = 3 WHERE material_didatico = 'New English File Elementary';
UPDATE alunos SET id_material_didatico = 4 WHERE material_didatico = 'English File Pre-Intermediate';

-- 3. Para outros materiais que possam existir, cria registros na tabela material_didatico
-- (Execute apenas se necessário)
-- INSERT INTO material_didatico (nome, editora, autor, obs, status, id_unidade, data_criacao, data_atualizacao) 
-- VALUES ('Nome do Material', 'Editora', 'Autor', 'Observações', 1, 1, NOW(), NOW());

-- 4. Adiciona a chave estrangeira
ALTER TABLE alunos ADD CONSTRAINT fk_alunos_material_didatico 
FOREIGN KEY (id_material_didatico) REFERENCES material_didatico(id);

-- 5. Remove a coluna antiga (CUIDADO: isso apagará os dados existentes)
-- ALTER TABLE alunos DROP COLUMN material_didatico;

-- 6. Verifica se a estrutura está correta
-- DESCRIBE alunos;
-- SELECT id, nome, id_material_didatico FROM alunos LIMIT 5;
