-- Script para testar se o campo valor mensalidade está sendo salvo
-- Execute este script após tentar salvar um aluno

-- 1. Verificar a estrutura da tabela
DESCRIBE alunos;

-- 2. Verificar se a coluna vlr_mensalidade existe e seu tipo
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, NUMERIC_PRECISION, NUMERIC_SCALE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'alunos'
AND COLUMN_NAME = 'vlr_mensalidade';

-- 3. Verificar os últimos registros inseridos
SELECT id, nome, vlr_mensalidade, id_material_didatico, status, bolsista, data_criacao
FROM alunos
ORDER BY data_criacao DESC
LIMIT 5;

-- 4. Verificar se há valores diferentes de 0 ou null
SELECT id, nome, vlr_mensalidade, 
       CASE 
         WHEN vlr_mensalidade IS NULL THEN 'NULL'
         WHEN vlr_mensalidade = 0 THEN 'ZERO'
         ELSE 'VALOR'
       END as status_valor
FROM alunos
ORDER BY data_criacao DESC
LIMIT 10;

-- 5. Verificar se há algum problema com a coluna
SELECT COUNT(*) as total_alunos,
       COUNT(vlr_mensalidade) as com_valor,
       COUNT(CASE WHEN vlr_mensalidade IS NULL THEN 1 END) as sem_valor,
       COUNT(CASE WHEN vlr_mensalidade = 0 THEN 1 END) as valor_zero
FROM alunos;

-- 6. Tentar inserir um valor de teste diretamente
-- INSERT INTO alunos (nome, cpf, celular, vlr_mensalidade, status, bolsista, id_unidade, data_criacao, data_atualizacao)
-- VALUES ('TESTE VALOR', '123.456.789-00', '(11) 99999-9999', 150.50, 1, false, 1, NOW(), NOW());

-- 7. Verificar se a inserção funcionou
-- SELECT id, nome, vlr_mensalidade FROM alunos WHERE nome = 'TESTE VALOR';



