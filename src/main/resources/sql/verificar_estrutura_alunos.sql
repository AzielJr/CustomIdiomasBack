-- Script para verificar a estrutura da tabela alunos
-- Execute este script para diagnosticar problemas

-- 1. Verificar se a tabela existe
SHOW TABLES LIKE 'alunos';

-- 2. Verificar a estrutura completa da tabela
DESCRIBE alunos;

-- 3. Verificar se a coluna vlr_mensalidade existe e seu tipo
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, NUMERIC_PRECISION, NUMERIC_SCALE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'alunos'
AND COLUMN_NAME = 'vlr_mensalidade';

-- 4. Verificar se há dados na tabela
SELECT COUNT(*) as total_alunos FROM alunos;

-- 5. Verificar alguns registros para ver o que está sendo salvo
SELECT id, nome, vlr_mensalidade, id_material_didatico, status, bolsista
FROM alunos
LIMIT 5;

-- 6. Verificar se a coluna id_material_didatico existe
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'alunos'
AND COLUMN_NAME = 'id_material_didatico';

-- 7. Verificar se há chaves estrangeiras
SELECT 
    CONSTRAINT_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'alunos'
AND REFERENCED_TABLE_NAME IS NOT NULL;

