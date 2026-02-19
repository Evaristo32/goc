#!/bin/bash

echo "=========================================="
echo "VERIFICAÇÃO COMPLETA DO BANCO DE DADOS"
echo "=========================================="
echo ""

sleep 30

echo "Tabelas criadas:"
docker exec goc-postgres psql -U goc -d goc -c "SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename;" 2>&1
echo ""

echo "Sequences criadas:"
docker exec goc-postgres psql -U goc -d goc -c "SELECT sequencename FROM pg_sequences WHERE schemaname='public' ORDER BY sequencename;" 2>&1
echo ""

echo "Estrutura da tabela CLIENTE:"
docker exec goc-postgres psql -U goc -d goc -c "\d cliente" 2>&1
echo ""

echo "Estrutura da tabela PRODUTO:"
docker exec goc-postgres psql -U goc -d goc -c "\d produto" 2>&1
echo ""

echo "Estrutura da tabela ORCAMENTOS:"
docker exec goc-postgres psql -U goc -d goc -c "\d orcamentos" 2>&1
echo ""

echo "Estrutura da tabela ITENSORCAMENTO:"
docker exec goc-postgres psql -U goc -d goc -c "\d itensorcamento" 2>&1
echo ""

echo "=========================================="
echo "VERIFICAÇÃO FINAL DE LOGS"
echo "=========================================="
echo ""

cd /Users/evaristodev/desenvolvimento/projetos/pessoal/goc
echo "Status dos containers:"
docker compose ps 2>&1
echo ""

echo "Última mensagem de sucesso da aplicação:"
docker compose logs goc-app 2>&1 | grep -i "migration\|Started\|table" | tail -10

