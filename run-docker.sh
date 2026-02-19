#!/bin/bash

# Script para compilar e executar o projeto com Docker

cd /Users/evaristodev/desenvolvimento/projetos/pessoal/goc

# Compilar o projeto
echo "Compilando o projeto..."
./mvnw clean package -DskipTests

# Criar imagem Docker
echo "Criando imagem Docker..."
docker build -t goc:latest .

# Iniciar os containers
echo "Iniciando containers..."
docker compose up -d

# Esperar um pouco para os containers iniciarem
sleep 10

# Mostrar status
echo "Status dos containers:"
docker compose ps

# Mostrar logs
echo "Logs do container goc-app:"
docker compose logs goc-app

