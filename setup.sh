#!/bin/bash

echo "=========================================="
echo "GOC - Script de Inicialização Completa"
echo "=========================================="
echo ""

# Compilar
echo "1️⃣  Compilando projeto..."
cd /Users/evaristodev/desenvolvimento/projetos/pessoal/goc
./mvnw clean package -DskipTests -q

# Build Docker
echo "2️⃣  Construindo imagem Docker..."
docker build -t goc:latest . -q

# Parar containers antigos
echo "3️⃣  Removendo containers e volumes antigos..."
docker compose down -v -q 2>/dev/null || true

# Aguardar
sleep 5

# Subir containers
echo "4️⃣  Subindo containers..."
docker compose up -d -q

# Aguardar PostgreSQL estar pronto
echo "5️⃣  Aguardando PostgreSQL estar pronto..."
sleep 15

# Executar init.sql
echo "6️⃣  Criando tabelas do banco de dados..."
docker cp /Users/evaristodev/desenvolvimento/projetos/pessoal/goc/init.sql goc-postgres:/init.sql -q 2>/dev/null || true
docker exec goc-postgres psql -U goc -d goc -f /init.sql > /dev/null 2>&1

# Verificar resultado
echo ""
echo "=========================================="
echo "✅ RESULTADO FINAL"
echo "=========================================="
echo ""
echo "Tabelas criadas:"
docker exec goc-postgres psql -U goc -d goc -c "SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename;" 2>&1
echo ""

echo "Sequences criadas:"
docker exec goc-postgres psql -U goc -d goc -c "SELECT sequencename FROM pg_sequences WHERE schemaname='public' ORDER BY sequencename;" 2>&1
echo ""

echo "Status dos containers:"
docker compose ps 2>&1 | grep -E "goc-|STATUS"
echo ""

echo "=========================================="
echo "🎉 Aplicação pronta para uso!"
echo "=========================================="
echo ""
echo "Endpoints:"
echo "  - API: http://localhost:8080"
echo "  - Health: http://localhost:8080/actuator/health"
echo "  - PostgreSQL: localhost:5437"
echo ""

