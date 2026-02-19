#!/bin/bash

echo "Aguardando 20 segundos para os containers iniciarem..."
sleep 20

echo "=========================================="
echo "Verificando Tabelas do Banco de Dados"
echo "=========================================="
echo ""

# Verificar as tabelas
echo "Tabelas criadas:"
docker exec goc-postgres psql -U goc -d goc -c "\dt" 2>&1
echo ""

echo "Sequences criadas:"
docker exec goc-postgres psql -U goc -d goc -c "\ds" 2>&1
echo ""

echo "Esquema do cliente:"
docker exec goc-postgres psql -U goc -d goc -c "\d cliente" 2>&1
echo ""

echo "Logs da aplicação (últimas 50 linhas):"
cd /Users/evaristodev/desenvolvimento/projetos/pessoal/goc
docker compose logs goc-app 2>&1 | tail -50

