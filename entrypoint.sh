#!/bin/bash

# Variáveis padrão
DB_HOST=${DB_HOST:-db}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-goc}
DB_USER=${DB_USER:-goc}
DB_PASSWORD=${DB_PASSWORD:-goc_pass}
JAVA_OPTS=${JAVA_OPTS:-"-Xms256m -Xmx512m"}

echo "=========================================="
echo "Iniciando aplicação GOC"
echo "=========================================="
echo "DB_HOST: $DB_HOST"
echo "DB_PORT: $DB_PORT"
echo "DB_NAME: $DB_NAME"
echo "DB_USER: $DB_USER"

# Aguardar PostgreSQL estar pronto
echo ""
echo "Aguardando PostgreSQL estar pronto..."
for i in {1..60}; do
  if pg_isready -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" > /dev/null 2>&1; then
    echo "✓ PostgreSQL está pronto!"
    break
  fi
  echo "  Tentativa $i/60..."
  sleep 2
done

# Aguardar um pouco a mais para garantir que o banco está totalmente pronto
sleep 3

# Executar o script de inicialização do banco de dados
if [ -f /app/init.sql ]; then
  echo ""
  echo "Executando script de inicialização do banco de dados..."
  export PGPASSWORD="$DB_PASSWORD"
  psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f /app/init.sql
  if [ $? -eq 0 ]; then
    echo "✓ Script de inicialização concluído com sucesso!"
  else
    echo "⚠ Script de inicialização teve problemas, continuando..."
  fi
  unset PGPASSWORD
else
  echo "⚠ Script /app/init.sql não encontrado"
fi

echo ""
echo "Iniciando a aplicação..."
echo "=========================================="
echo ""

exec java $JAVA_OPTS -jar /app/app.jar

