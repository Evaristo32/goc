-- Migration consolidada: cria todas as tabelas, sequences e índices
-- Drops para garantir limpeza
DROP TABLE IF EXISTS itensorcamento CASCADE;
DROP TABLE IF EXISTS orcamentos CASCADE;
DROP TABLE IF EXISTS produto CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP SEQUENCE IF EXISTS seq_cliente CASCADE;
DROP SEQUENCE IF EXISTS seq_produto CASCADE;
DROP SEQUENCE IF EXISTS seq_orcamento CASCADE;
DROP SEQUENCE IF EXISTS seq_itens_orcamento CASCADE;

-- Criação das sequences
CREATE SEQUENCE seq_cliente START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_produto START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_orcamento START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_itens_orcamento START WITH 1 INCREMENT BY 1;

-- Tabela cliente
CREATE TABLE cliente (
    clienteid BIGINT PRIMARY KEY DEFAULT nextval('seq_cliente'),
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    endereco VARCHAR(255)
);

-- Tabela produto
CREATE TABLE produto (
    produtoid BIGINT PRIMARY KEY DEFAULT nextval('seq_produto'),
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(150) NOT NULL,
    preco NUMERIC(10, 2) NOT NULL
);

-- Tabela orcamentos
CREATE TABLE orcamentos (
    orcamentoid BIGINT PRIMARY KEY DEFAULT nextval('seq_orcamento'),
    data DATE NOT NULL,
    validade DATE NOT NULL,
    status VARCHAR(255) NOT NULL,
    total NUMERIC(10, 2) NOT NULL,
    clienteid BIGINT NOT NULL REFERENCES cliente(clienteid)
);

-- Tabela itensorcamento
CREATE TABLE itensorcamento (
    itemid BIGINT PRIMARY KEY DEFAULT nextval('seq_itens_orcamento'),
    quantidade INTEGER NOT NULL,
    preco NUMERIC(10, 2) NOT NULL,
    produtoid BIGINT NOT NULL REFERENCES produto(produtoid),
    orcamentoid BIGINT NOT NULL REFERENCES orcamentos(orcamentoid)
);

-- Índices
CREATE INDEX idx_cliente_email ON cliente(email);
CREATE INDEX idx_orcamentos_clienteid ON orcamentos(clienteid);
CREATE INDEX idx_orcamentos_status ON orcamentos(status);
CREATE INDEX idx_itensorcamento_produtoid ON itensorcamento(produtoid);
CREATE INDEX idx_itensorcamento_orcamentoid ON itensorcamento(orcamentoid);

-- Constraints
ALTER TABLE orcamentos ADD CONSTRAINT chk_orcamento_datas CHECK (data <= validade);

