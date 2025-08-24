-- Script para adicionar os novos campos na tabela alunos
-- Execute este script no seu banco de dados MySQL

-- Adicionar campo numero
ALTER TABLE alunos ADD COLUMN numero VARCHAR(20) AFTER endereco;

-- Adicionar campo complemento
ALTER TABLE alunos ADD COLUMN complemento VARCHAR(100) AFTER numero;

-- Atualizar registros existentes (opcional)
UPDATE alunos SET numero = '', complemento = '' WHERE numero IS NULL OR complemento IS NULL;

-- Verificar a estrutura da tabela
DESCRIBE alunos;
