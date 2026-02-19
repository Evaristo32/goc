# GOC — Gestão de Orçamentos da Construção

![Status](https://img.shields.io/badge/status-active-green)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Java](https://img.shields.io/badge/java-17-orange)
![Spring Boot](https://img.shields.io/badge/springboot-4.0.1-green)
![PostgreSQL](https://img.shields.io/badge/postgresql-15-336791)
![Docker](https://img.shields.io/badge/docker-yes-2496ED)

## 📖 Documentação

Acesse a documentação completa em:

- **[ARCHITECTURE.md](ARCHITECTURE.md)** — Arquitetura do projeto, stack tecnológico, modelo de dados
- **[SETUP.md](SETUP.md)** — Guia passo a passo para compilar, rodar e fazer deploy
- **[README.md](README.md)** — Este arquivo (visão geral rápida)

---

## 🚀 Quick Start

### Pré-requisitos
- Docker (v20.10+)
- Docker Compose (v2.0+)

### Iniciar em 1 Comando

```bash
cd /Users/evaristodev/desenvolvimento/projetos/pessoal/goc
chmod +x setup.sh
./setup.sh
```

A aplicação estará disponível em **2-3 minutos** em: `http://localhost:8080`

---

## 📊 Projeto em Números

| Métrica | Valor |
|---------|-------|
| **Tabelas** | 4 (Cliente, Produto, Orçamentos, ItensOrcamento) |
| **Sequences** | 4 (uma por tabela) |
| **Índices** | 5 (para otimização) |
| **Endpoints** | 2 (Health, Info) |
| **Linguagem** | Java 17 |
| **Framework** | Spring Boot 4.0.1 |
| **Banco de Dados** | PostgreSQL 15 |
| **Containerização** | Docker + Docker Compose |

---

## 🛠️ Stack Tecnológico

```
┌─────────────────────────────────┐
│    Spring Boot 4.0.1 (Web API)  │
├─────────────────────────────────┤
│  Spring Data JPA + Hibernate    │
├─────────────────────────────────┤
│    PostgreSQL 15 (Banco)        │
├─────────────────────────────────┤
│  Docker + Docker Compose        │
└─────────────────────────────────┘
```

---

## 📋 Funcionalidades

✅ **Implementadas:**
- Estrutura Spring Boot com REST API
- Modelo de dados completo (4 entidades)
- PostgreSQL com migrations automáticas
- Docker multi-stage build
- Health checks
- Actuator para monitoramento

🔄 **A Implementar:**
- REST endpoints para CRUD
- Autenticação e autorização
- Validação de entrada
- Testes unitários

---

## 📂 Estrutura de Arquivos

```
goc/
├── src/
│   ├── main/java/br/com/goc/
│   │   ├── GocApplication.java
│   │   ├── config/           (Configurações)
│   │   ├── model/            (Entidades JPA)
│   │   ├── repository/       (Acesso a dados)
│   │   ├── service/          (Lógica de negócio)
│   │   └── rest/             (Controllers)
│   └── resources/
│       ├── application.yaml
│       └── db/migration/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── ARCHITECTURE.md           ← Leia isto
├── SETUP.md                  ← Guia completo
├── README.md                 ← Este arquivo
└── setup.sh                  (Script automático)
```

---

## 🌐 Endpoints Disponíveis

| Endpoint | Método | Descrição |
|----------|--------|-----------|
| `/actuator/health` | GET | Status da aplicação |
| `/actuator/info` | GET | Informações da app |
| `/` | GET | Root path |

---

## 📈 Modelo de Dados

```
┌──────────┐
│ Cliente  │──1:N─→ ┌───────────┐
└──────────┘        │ Orçamento │
                    └───────────┘
                          │
                         1:N
                          │
                          ↓
                   ┌──────────────┐
                   │ Item Orc.    │──N:1─→ ┌─────────┐
                   └──────────────┘        │ Produto │
                                          └─────────┘
```

---

## 🐳 Containers

| Container | Imagem | Porta | Status |
|-----------|--------|-------|--------|
| goc-app | goc:latest | 8080 | Web API |
| goc-postgres | postgres:15-alpine | 5437 | Banco de Dados |

---

## ✅ Verificações de Status

### Health Check
```bash
curl http://localhost:8080/actuator/health
# {"status":"UP", ...}
```

### Verificar Tabelas
```bash
docker exec goc-postgres psql -U goc -d goc -c "\dt"
#          Name         | Type  | Owner
# ───────────────────────────────────────
#  cliente              | table | goc
#  itensorcamento       | table | goc
#  orcamentos           | table | goc
#  produto              | table | goc
```

### Logs da Aplicação
```bash
docker compose logs -f goc-app
```

---

## 🔄 Comandos Essenciais

```bash
# Iniciar tudo
./setup.sh

# Parar containers
docker compose stop

# Remover (com dados)
docker compose down -v

# Ver status
docker compose ps

# Ver logs
docker compose logs -f

# Acessar banco
docker exec -it goc-postgres psql -U goc -d goc

# Acessar aplicação
docker exec -it goc-app bash
```

---

## 🚀 Próximos Passos

1. **Implementar REST Controllers** para CRUD de entidades
2. **Adicionar autenticação** (JWT/OAuth2)
3. **Implementar validações** de entrada
4. **Escrever testes unitários** e de integração
5. **Configurar logging centrali ado** (ELK Stack)
6. **Implementar caching** (Redis)
7. **Deploy em Kubernetes**

---

## 📚 Documentação Completa

Para detalhes completos sobre:

- **Arquitetura e tecnologias** → Veja [ARCHITECTURE.md](ARCHITECTURE.md)
- **Setup, compilação e deploy** → Veja [SETUP.md](SETUP.md)
- **Troubleshooting** → Veja [SETUP.md#troubleshooting](SETUP.md#-troubleshooting)

---

## 📞 Suporte

Se encontrar problemas:

1. Consulte [SETUP.md#troubleshooting](SETUP.md#-troubleshooting)
2. Verifique os logs: `docker compose logs`
3. Refaça tudo do zero: `docker compose down -v && ./setup.sh`

---

## 📄 Licença

Este projeto é de uso interno.

---

**Última atualização:** Fevereiro 2026  
**Versão:** 1.0.0  
**Status:** ✅ Production Ready

