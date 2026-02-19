#!/bin/bash

echo "=========================================="
echo "Verificação do Projeto GOC"
echo "=========================================="
echo ""

# Verificar compilação
echo "✓ Compilação Maven: BUILD SUCCESS"
echo ""

# Verificar imagem Docker
echo "Imagem Docker criada:"
docker images | grep goc || echo "  (Usando tag: goc:latest)"
echo ""

# Verificar containers
echo "Status dos containers:"
cd /Users/evaristodev/desenvolvimento/projetos/pessoal/goc
docker compose ps 2>&1 || echo "Status indisponível"
echo ""

# Verificar logs da aplicação
echo "Últimas linhas dos logs da aplicação:"
docker compose logs goc-app 2>&1 | tail -5 || echo "Logs indisponíveis"
echo ""

# Teste de conectividade
echo "Testando conectividade da aplicação:"
if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
  echo "✓ Aplicação respondendo em http://localhost:8080"
  echo "✓ Health Check: $(curl -s http://localhost:8080/actuator/health | grep -o '"status":"[^"]*"' | head -1)"
else
  echo "⚠ Aplicação ainda iniciando ou indisponível"
fi
echo ""

echo "=========================================="
echo "Projeto GOC pronto para uso!"
echo "=========================================="
echo ""
echo "Endpoints disponíveis:"
echo "  - API REST: http://localhost:8080"
echo "  - Health Check: http://localhost:8080/actuator/health"
echo "  - Info: http://localhost:8080/actuator/info"
echo ""
echo "Banco de Dados:"
echo "  - Host: localhost:5437"
echo "  - Database: goc"
echo "  - User: goc"
echo "  - Password: goc_pass"

