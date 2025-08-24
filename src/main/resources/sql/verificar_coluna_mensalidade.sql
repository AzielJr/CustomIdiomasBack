-- Script para verificar se a coluna vlr_mensalidade existe e tem o tipo correto
-- Execute este script no seu banco de dados para verificar a estrutura

-- Verificar se a coluna existe
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'alunos' 
AND COLUMN_NAME = 'vlr_mensalidade';

-- Verificar a estrutura completa da tabela
DESCRIBE alunos;

-- Verificar se há dados na coluna
SELECT id, nome, vlr_mensalidade 
FROM alunos 
LIMIT 5;

-- Se a coluna não existir ou tiver tipo incorreto, execute:
-- ALTER TABLE alunos MODIFY COLUMN vlr_mensalidade DECIMAL(10,2);
