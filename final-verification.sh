#!/bin/bash

echo "=========================================="
echo "VERIFICAÇÃO FINAL - TABELAS DO BANCO"
echo "=========================================="
echo ""

# Aguardar os containers estarem prontos
echo "Aguardando 30 segundos para os containers iniciarem..."
sleep 30

# Verificar tabelas
echo "📊 Tabelas criadas:"
docker exec goc-postgres psql -U goc -d goc -c "SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename;" 2>&1
echo ""

# Verificar sequences
echo "🔢 Sequences criadas:"
docker exec goc-postgres psql -U goc -d goc -c "SELECT sequencename FROM pg_sequences WHERE schemaname='public' ORDER BY sequencename;" 2>&1
echo ""

# Verificar estrutura de cada tabela
echo "📋 Estrutura das tabelas:"
for table in cliente produto orcamentos itensorcamento; do
    echo ""
    echo "Tabela: $table"
    docker exec goc-postgres psql -U goc -d goc -c "\d $table" 2>&1 | head -20
done

echo ""
echo "=========================================="
echo "✅ VERIFICAÇÃO COMPLETA"
echo "=========================================="

