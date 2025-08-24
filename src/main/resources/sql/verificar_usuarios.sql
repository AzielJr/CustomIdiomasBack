-- Script para verificar e corrigir usuários sem unidade
-- Execute este script no seu banco MySQL

-- 1. Verificar usuários existentes e suas unidades
SELECT 
    u.id,
    u.email,
    u.user_name,
    u.id_unidade,
    un.fantasia as nome_unidade
FROM usuarios u
LEFT JOIN unidades un ON u.id_unidade = un.id
ORDER BY u.id;

-- 2. Se algum usuário não tiver unidade, atualizar para unidade padrão (ID 1)
-- Descomente e execute a linha abaixo se necessário:
-- UPDATE usuarios SET id_unidade = 1 WHERE id_unidade IS NULL OR id_unidade = 0;

-- 3. Verificar se a unidade padrão existe
SELECT * FROM unidades WHERE id = 1;

-- 4. Se não existir unidade, criar uma padrão
-- INSERT INTO unidades (id, fantasia, razao_social, cnpj, ativo, data_criacao) 
-- VALUES (1, 'Unidade Padrão', 'Unidade Padrão', '00000000000000', 1, NOW());
