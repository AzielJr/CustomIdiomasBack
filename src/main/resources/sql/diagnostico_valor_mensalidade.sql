-- Script de diagnóstico para o campo valor mensalidade
-- Execute este script para identificar o problema

-- 1. Verificar se a tabela existe
SHOW TABLES LIKE 'alunos';

-- 2. Verificar a estrutura completa da tabela
DESCRIBE alunos;

-- 3. Verificar especificamente a coluna vlr_mensalidade
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    NUMERIC_PRECISION,
    NUMERIC_SCALE,
    COLUMN_TYPE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'alunos'
AND COLUMN_NAME = 'vlr_mensalidade';

-- 4. Verificar se há dados na tabela
SELECT COUNT(*) as total_registros FROM alunos;

-- 5. Verificar os últimos 5 registros com foco no valor mensalidade
SELECT 
    id,
    nome,
    vlr_mensalidade,
    id_material_didatico,
    status,
    bolsista,
    data_criacao,
    data_atualizacao
FROM alunos
ORDER BY data_criacao DESC
LIMIT 5;

-- 6. Verificar se há valores diferentes de 0 ou null
SELECT 
    id,
    nome,
    vlr_mensalidade,
    CASE 
        WHEN vlr_mensalidade IS NULL THEN 'NULL'
        WHEN vlr_mensalidade = 0 THEN 'ZERO'
        ELSE CONCAT('VALOR: ', vlr_mensalidade)
    END as status_valor
FROM alunos
ORDER BY data_criacao DESC
LIMIT 10;

-- 7. Estatísticas da coluna vlr_mensalidade
SELECT 
    COUNT(*) as total_alunos,
    COUNT(vlr_mensalidade) as com_valor,
    COUNT(CASE WHEN vlr_mensalidade IS NULL THEN 1 END) as sem_valor,
    COUNT(CASE WHEN vlr_mensalidade = 0 THEN 1 END) as valor_zero,
    COUNT(CASE WHEN vlr_mensalidade > 0 THEN 1 END) as valor_positivo
FROM alunos;

-- 8. Verificar se há problemas de tipo de dados
SELECT 
    id,
    nome,
    vlr_mensalidade,
    CASE 
        WHEN vlr_mensalidade IS NULL THEN 'NULL'
        WHEN vlr_mensalidade = 0 THEN 'ZERO'
        WHEN vlr_mensalidade > 0 THEN 'POSITIVO'
        ELSE 'NEGATIVO'
    END as tipo_valor,
    LENGTH(CAST(vlr_mensalidade AS CHAR)) as tamanho_string
FROM alunos
WHERE vlr_mensalidade IS NOT NULL
ORDER BY data_criacao DESC
LIMIT 5;

-- 9. Tentar inserir um valor de teste diretamente
-- DESCOMENTE AS LINHAS ABAIXO PARA TESTAR
/*
INSERT INTO alunos (
    nome, cpf, celular, vlr_mensalidade, status, bolsista, 
    id_unidade, data_criacao, data_atualizacao
) VALUES (
    'TESTE VALOR MSG', '123.456.789-00', '(11) 99999-9999', 
    150.50, 1, false, 1, NOW(), NOW()
);

-- Verificar se a inserção funcionou
SELECT id, nome, vlr_mensalidade FROM alunos WHERE nome = 'TESTE VALOR MSG';
*/

-- 10. Verificar se há constraints ou triggers
SELECT 
    CONSTRAINT_NAME,
    CONSTRAINT_TYPE,
    TABLE_NAME,
    COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'alunos'
AND COLUMN_NAME = 'vlr_mensalidade';

-- 11. Verificar se há índices na coluna
SHOW INDEX FROM alunos WHERE Column_name = 'vlr_mensalidade';



