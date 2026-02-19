#!/bin/bash

# Script para iniciar o projeto GOC com Docker

echo "========================================="
echo "GOC - Gestão de Orçamentos da Construção"
echo "========================================="
echo ""

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não está instalado!"
    exit 1
fi

echo "✅ Docker encontrado"
echo ""

# Navegar para o diretório do projeto
cd "$(dirname "$0")" || exit

echo "📁 Diretório: $(pwd)"
echo ""

# Parar containers anteriores se existirem
echo "🛑 Parando containers anteriores..."
docker compose down -v 2>/dev/null || true
echo ""

# Iniciar os containers
echo "🚀 Iniciando containers..."
docker compose up -d

if [ $? -ne 0 ]; then
    echo "❌ Erro ao iniciar os containers!"
    exit 1
fi

echo ""
echo "⏳ Aguardando PostgreSQL ficar pronto..."
sleep 10

# Verificar se PostgreSQL está rodando
if docker compose exec db pg_isready -U goc > /dev/null 2>&1; then
    echo "✅ PostgreSQL está pronto!"
else
    echo "⚠️  PostgreSQL pode estar ainda iniciando..."
fi

echo ""
echo "⏳ Aguardando aplicação iniciar..."
sleep 15

# Testar se a aplicação está respondendo
echo ""
echo "🔍 Testando saúde da aplicação..."
if curl -s http://localhost:8080/actuator/health > /dev/null; then
    echo "✅ Aplicação está respondendo!"
else
    echo "⚠️  Aplicação pode estar ainda iniciando..."
    echo "   Tente novamente em alguns segundos"
fi

echo ""
echo "========================================="
echo "✅ PROJETO INICIADO COM SUCESSO!"
echo "========================================="
echo ""
echo "📊 Informações de acesso:"
echo "  • API: http://localhost:8080"
echo "  • Health: http://localhost:8080/actuator/health"
echo "  • PostgreSQL: localhost:5432"
echo "     - User: goc"
echo "     - Password: goc_pass"
echo "     - Database: goc"
echo ""
echo "📖 Comandos úteis:"
echo "  • Ver logs: docker compose logs -f app"
echo "  • Parar: docker compose down"
echo "  • Parar e limpar: docker compose down -v"
echo ""

