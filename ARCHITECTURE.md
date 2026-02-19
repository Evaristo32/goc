# GOC — Arquitetura do Projeto

## 📋 Visão Geral

GOC (Gestão de Orçamentos da Construção) é uma aplicação web para gerenciar orçamentos de projetos de construção. O projeto utiliza uma arquitetura moderna com Spring Boot, PostgreSQL e Docker, seguindo as melhores práticas de desenvolvimento.

---

## 🏗️ Arquitetura Geral

```
┌─────────────────────────────────────────────────┐
│           Cliente HTTP (Browser)                │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│       Spring Boot Application (REST API)        │
│   - Spring Web MVC                              │
│   - Spring Data JPA                             │
│   - Hibernate ORM                               │
│   - Spring Boot Actuator                        │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│      PostgreSQL 15 (Banco de Dados)            │
│   - 4 Tabelas principais                        │
│   - Sequences para geração de IDs               │
│   - Índices para performance                    │
└─────────────────────────────────────────────────┘
```

---

## 🛠️ Stack Tecnológico

### **Backend**
| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| **Java** | 17 | Linguagem de programação |
| **Spring Boot** | 4.0.1 | Framework para aplicações web |
| **Spring Web** | 4.0.1 | Suporte a REST APIs |
| **Spring Data JPA** | 4.0.1 | Acesso a dados com Hibernate |
| **Hibernate ORM** | 7.2.0 | Mapeamento objeto-relacional |
| **Spring Boot Actuator** | 4.0.1 | Monitoramento e healthcheck |
| **PostgreSQL Driver** | Latest | Driver JDBC para PostgreSQL |
| **Flyway** | Latest | Versionamento de banco de dados |
| **Maven** | 3.9.4 | Gerenciador de dependências e build |

### **Banco de Dados**
| Componente | Detalhes |
|-----------|----------|
| **PostgreSQL** | v15 (Alpine Linux) |
| **Porta** | 5437 (externa) / 5432 (interna) |
| **Database** | `goc` |
| **User** | `goc` |
| **Password** | `goc_pass` |

### **Containerização**
| Componente | Detalhes |
|-----------|----------|
| **Docker** | Multi-stage build |
| **Docker Compose** | v3.8 |
| **Base Image** | eclipse-temurin:17-jre-jammy |
| **Build Image** | maven:3.9.4-eclipse-temurin-17 |

---

## 📦 Modelo de Dados

### **Tabelas Principais**

#### **1. Cliente**
```sql
cliente (
  clienteid BIGINT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  email VARCHAR(150) NOT NULL UNIQUE,
  endereco VARCHAR(255)
)
```
- **Descrição**: Armazena informações dos clientes
- **Índices**: idx_cliente_email
- **Sequence**: seq_cliente

#### **2. Produto**
```sql
produto (
  produtoid BIGINT PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  descricao VARCHAR(150) NOT NULL,
  preco NUMERIC(10, 2) NOT NULL
)
```
- **Descrição**: Catálogo de produtos/serviços
- **Sequence**: seq_produto

#### **3. Orcamentos**
```sql
orcamentos (
  orcamentoid BIGINT PRIMARY KEY,
  data DATE NOT NULL,
  validade DATE NOT NULL,
  status VARCHAR(255) NOT NULL,
  total NUMERIC(10, 2) NOT NULL,
  clienteid BIGINT NOT NULL (FK -> cliente)
)
```
- **Descrição**: Orçamentos emitidos para clientes
- **Índices**: idx_orcamentos_clienteid, idx_orcamentos_status
- **Sequence**: seq_orcamento
- **Constraint**: data <= validade

#### **4. ItensOrcamento**
```sql
itensorcamento (
  itemid BIGINT PRIMARY KEY,
  quantidade INTEGER NOT NULL,
  preco NUMERIC(10, 2) NOT NULL,
  produtoid BIGINT NOT NULL (FK -> produto),
  orcamentoid BIGINT NOT NULL (FK -> orcamentos)
)
```
- **Descrição**: Itens que compõem cada orçamento
- **Índices**: idx_itensorcamento_produtoid, idx_itensorcamento_orcamentoid
- **Sequence**: seq_itens_orcamento

---

## 📂 Estrutura do Projeto

```
goc/
├── src/
│   ├── main/
│   │   ├── java/br/com/goc/
│   │   │   ├── GocApplication.java          # Classe principal Spring Boot
│   │   │   ├── config/
│   │   │   │   ├── FlywayConfig.java        # Configuração do Flyway
│   │   │   │   ├── FlywayRunner.java        # Executor de migrations
│   │   │   │   └── StartupCheck.java        # Verificação ao startup
│   │   │   ├── model/
│   │   │   │   ├── Cliente.java             # Entidade Cliente
│   │   │   │   ├── Produto.java             # Entidade Produto
│   │   │   │   ├── Orcamento.java           # Entidade Orçamento
│   │   │   │   └── ItensOrcamento.java      # Entidade Item Orçamento
│   │   │   ├── repository/                  # Camada de acesso a dados (vazio)
│   │   │   ├── service/                     # Camada de lógica de negócio (vazio)
│   │   │   └── rest/                        # Controllers REST (vazio)
│   │   └── resources/
│   │       ├── application.yaml              # Configuração da aplicação
│   │       └── db/migration/
│   │           └── V1__create_all_tables.sql # Script de inicialização do BD
│   └── test/
│       └── GocApplicationTests.java
├── Dockerfile                                # Multi-stage Docker build
├── docker-compose.yml                        # Orquestração de containers
├── entrypoint.sh                             # Script de inicialização
├── init.sql                                  # Script de criação de tabelas
├── pom.xml                                   # Configuração Maven
├── README.md                                 # Este arquivo
├── ARCHITECTURE.md                           # Documentação de arquitetura
└── SETUP.md                                  # Guia de setup e deploy
```

---

## 🔄 Fluxo de Requisição

```
1. Cliente envia requisição HTTP
         ↓
2. Spring DispatcherServlet recebe requisição
         ↓
3. Controller processa a requisição (REST)
         ↓
4. Service executa lógica de negócio
         ↓
5. Repository (JPA) acessa dados via Hibernate
         ↓
6. Hibernate mapeia entidades para SQL
         ↓
7. PostgreSQL executa query
         ↓
8. Resultado retorna para o Controller
         ↓
9. Resposta JSON é enviada ao cliente
```

---

## 🐳 Arquitetura Docker

### **Dockerfile (Multi-stage)**

**Stage 1: Build**
- Base: `maven:3.9.4-eclipse-temurin-17`
- Compila o projeto Maven
- Gera JAR executável

**Stage 2: Runtime**
- Base: `eclipse-temurin:17-jre-jammy`
- Copia JAR do stage 1
- Instala cliente PostgreSQL
- Define entrypoint e startup

### **Docker Compose Services**

#### **db (PostgreSQL)**
```yaml
- Image: postgres:15-alpine
- Container: goc-postgres
- Porta: 5437:5432
- Volume: db-data (persistência)
- Healthcheck: pg_isready
- Network: goc-network
```

#### **app (Spring Boot)**
```yaml
- Build: ./Dockerfile
- Container: goc-app
- Porta: 8080:8080
- Depends on: db (service_healthy)
- Network: goc-network
- Entrypoint: /app/entrypoint.sh
```

---

## ⚙️ Configuração da Aplicação

### **application.yaml**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://db:5432/goc
    username: goc
    password: goc_pass
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        dialect: PostgreSQLDialect
  
  flyway:
    enabled: false
```

### **Variáveis de Ambiente**
- `DB_HOST`: Host do PostgreSQL (padrão: db)
- `DB_PORT`: Porta do PostgreSQL (padrão: 5432)
- `DB_NAME`: Nome do banco (padrão: goc)
- `DB_USER`: Usuário do banco (padrão: goc)
- `DB_PASSWORD`: Senha do banco (padrão: goc_pass)
- `JAVA_OPTS`: Opções JVM (padrão: -Xms256m -Xmx512m)

---

## 📊 Diagrama de Relacionamento (ER)

```
┌─────────────┐         ┌──────────────┐
│   Cliente   │────────│  Orcamentos  │
└─────────────┘    1:N └──────────────┘
                             │
                             │ 1:N
                             ▼
                   ┌──────────────────┐
                   │ ItensOrcamento   │
                   └──────────────────┘
                             │
                             │ N:1
                             ▼
                        ┌─────────────┐
                        │  Produto    │
                        └─────────────┘
```

---

## 🚀 Endpoints Disponíveis

| Recurso | Endpoint | Método | Descrição |
|---------|----------|--------|-----------|
| Health Check | `/actuator/health` | GET | Status da aplicação |
| Info | `/actuator/info` | GET | Informações da aplicação |
| Root | `/` | GET | Raiz da aplicação |

*Nota: Endpoints para CRUD de Cliente, Produto, Orçamento e Itens ainda não estão implementados.*

---

## 📈 Performance e Escalabilidade

### **Otimizações Implementadas**
- ✅ Índices no banco para queries frequentes
- ✅ Connection pooling com HikariCP
- ✅ Lazy loading em relacionamentos JPA
- ✅ Caching de entidades Hibernate
- ✅ Heap size configurável via JAVA_OPTS

### **Possíveis Melhorias Futuras**
- 📍 Redis para caching de sessão
- 📍 Query optimization e análise de performance
- 📍 Implementação de paginação
- 📍 API Gateway para roteamento
- 📍 Load balancing com múltiplas instâncias

---

## 🔐 Segurança

### **Implementado**
- ✅ PostgreSQL em rede isolada (Docker network)
- ✅ Senha padrão para desenvolvimento (mude em produção)
- ✅ JPA para prevenir SQL injection
- ✅ CORS configurável no Spring

### **Recomendações para Produção**
- 🔒 Implementar autenticação (JWT/OAuth2)
- 🔒 HTTPS/TLS obrigatório
- 🔒 Usar Docker secrets para credenciais
- 🔒 Rate limiting nas APIs
- 🔒 Validação de entrada em todos os endpoints
- 🔒 Audit logging

---

## 📝 Logging

```
Nível: DEBUG
Framework: SLF4J + Logback
Outputs: Console (Docker logs)

Componentes com debug ativado:
- org.flywaydb
- org.springframework.boot.autoconfigure.flyway
```

---

## 🧪 Testes

- Unit Tests: `src/test/java/br/com/goc/GocApplicationTests.java`
- Framework: JUnit 5 com Spring Boot Test
- Execução: `./mvnw test`

---

## 📚 Dependências Principais

Veja `pom.xml` para lista completa. Principais:
- spring-boot-starter-data-jpa
- spring-boot-starter-web
- spring-boot-starter-actuator
- postgresql (driver)
- flyway-core
- jakarta.persistence-api

---

## 🔗 Links Úteis

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [PostgreSQL Docs](https://www.postgresql.org/docs/)
- [Docker Docs](https://docs.docker.com/)
- [Hibernate ORM](https://hibernate.org/orm/)

---

**Última atualização**: Fevereiro 2026  
**Versão**: 1.0.0

