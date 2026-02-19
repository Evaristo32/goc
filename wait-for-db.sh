#!/usr/bin/env sh
# simples wait-for-postgres: espera pelo host/porta e tenta conectar com pg_isready
set -e
host="${DB_HOST:-db}"
port="${DB_PORT:-5432}"
user="${DB_USER:-postgres}"

echo "Waiting for postgres at ${host}:${port}..."
# usar pg_isready se disponível
while ! pg_isready -h "$host" -p "$port" -U "$user" >/dev/null 2>&1; do
  echo "Postgres is not ready - sleeping"
  sleep 1
done

echo "Postgres is ready"
exec "$@"

