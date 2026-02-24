# GOC - Gestão de Orçamentos da Construção

## 📋 Visão Geral

Sistema REST API para gestão de orçamentos da construção, desenvolvido com Spring Boot 3.4.0, PostgreSQL 15 e Docker.

---

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.4.0**
- **PostgreSQL 15**
- **Docker & Docker Compose**
- **Maven**
- **JPA/Hibernate**
- **Flyway (Migrations)**

---

## 🚀 Primeira Execução

### Opção 1: Com Docker (Recomendado para Produção)

#### 1.1 Pré-requisitos
- Docker Desktop instalado e rodando
- Terminal/Git Bash

#### 1.2 Passos

**Passo 1:** Clonar/abrir o projeto
```bash
cd goc
```

**Passo 2:** Compilar o projeto
```bash
./mvnw clean package -DskipTests
```
*Aguarde 2-3 minutos para download de dependências e compilação*

**Passo 3:** Iniciar Docker Compose
```bash
docker compose up -d
```
*Isso inicia PostgreSQL na porta 5437 e a aplicação na porta 8080*

**Passo 4:** Aguardar inicialização
```bash
sleep 30
```
*Aguarde a aplicação iniciar completamente*

**Passo 5:** Verificar status
```bash
docker ps
docker logs goc-app --tail 20
```

**Passo 6:** Testar a API
```bash
# Health Check
curl http://localhost:8080/actuator/health

# Criar Cliente
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "endereco": "Rua Principal, 123"
  }'
```

**Passo 7:** Parar a aplicação
```bash
docker compose down
```

---

### Opção 2: Direto pela IDE (IntelliJ - Recomendado para Desenvolvimento)

#### 2.1 Pré-requisitos
- IntelliJ IDEA Community ou Ultimate
- Java 17+ instalado
- PostgreSQL rodando (via Docker ou instalado)

#### 2.2 Passos

**Passo 1:** Abrir o projeto
- `File` → `Open` → Selecione a pasta do projeto `goc`
- IntelliJ detectará automaticamente que é um projeto Maven
- Clique em `Trust Project` quando solicitado

**Passo 2:** Configurar o Java SDK
- `IntelliJ IDEA` → `Preferences` (macOS) ou `Settings` (Windows/Linux)
- `Project Structure` → `Project`
- Em `SDK`, selecione `Java 17+`
- Se não tiver, clique `Add SDK` → `Download JDK` → Escolha `Temurin 17.x`
- Clique `Apply` e `OK`

**Passo 3:** Carregar dependências Maven
- Clique com botão direito na raiz do projeto (pasta `goc`)
- `Maven` → `Reload Projects`
- Aguarde o download de todas as dependências (2-3 minutos)

**Passo 4:** Iniciar o PostgreSQL (se não estiver rodando)
```bash
# Abra um novo terminal e execute:
docker compose up -d db
```
*Isso inicia apenas o banco de dados na porta 5437*

**Passo 5:** Configurar Spring Boot Run Configuration
- No IntelliJ: `Run` → `Edit Configurations...`
- Clique `+` (adicionar nova configuração)
- Selecione `Spring Boot`
- Preencha:
  - **Name:** `GocApplication`
  - **Main class:** `br.com.goc.GocApplication`
  - **VM options:** (deixe em branco)
  - **Active profiles:** `dev` (opcional)
- Clique `Apply` e `OK`

**Passo 6:** Executar a aplicação
- Clique no botão ▶️ (Run) no topo direito do IntelliJ
- Ou pressione `Ctrl + R` (macOS: `Cmd + R`)
- Aguarde aparecer: `Tomcat started on port 8080`

**Passo 7:** Testar a API
```bash
# Health Check
curl http://localhost:8080/actuator/health

# Criar Cliente
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "endereco": "Rua Principal, 123"
  }'
```

**Passo 8:** Parar a aplicação
- Clique no botão ⏹️ (Stop) no IntelliJ
- Ou pressione `Ctrl + C` no terminal

---

### Opção 3: Pela Linha de Comando (Maven)

#### 3.1 Pré-requisitos
- Java 17+ instalado
- PostgreSQL rodando na porta 5437

#### 3.2 Passos

**Passo 1:** Navegue até o projeto
```bash
cd goc
```

**Passo 2:** Inicie o PostgreSQL (se necessário)
```bash
docker run -d \
  --name goc-postgres \
  -e POSTGRES_DB=goc \
  -e POSTGRES_USER=goc \
  -e POSTGRES_PASSWORD=goc_pass \
  -p 5437:5432 \
  postgres:15-alpine
```

**Passo 3:** Execute a aplicação
```bash
./mvnw spring-boot:run
```

**Passo 4:** Aguarde a mensagem
```
Tomcat started on port(s): 8080 (http)
```

**Passo 5:** Testar
```bash
curl http://localhost:8080/actuator/health
```

**Passo 6:** Parar
- Pressione `Ctrl + C` no terminal

---

## 📊 Comparação de Opções

| Aspecto | Docker | IDE (IntelliJ) | CLI |
|---------|--------|---|---|
| **Setup Inicial** | Moderado | Fácil | Fácil |
| **Desenvolvimento** | Difícil debugar | ⭐ Melhor | Médio |
| **Performance** | Isolado | Nativa | Nativa |
| **Banco Sincronizado** | ✅ Sim | ⚠️ Manual | ⚠️ Manual |
| **Ideal para** | Produção | Desenvolvimento | Testes rápidos |

---

## 🔧 Troubleshooting

### Erro: "Connection refused" ao tentar conectar no banco
**Solução:** PostgreSQL não está rodando
```bash
# Verifique:
docker ps | grep postgres

# Se não está rodando, inicie:
docker compose up -d db
```

### Erro: "Port 8080 already in use"
**Solução:** Outra aplicação está usando a porta
```bash
# Libere a porta:
lsof -i :8080
kill -9 <PID>

# Ou use outra porta no application.yaml:
# server:
#   port: 8081
```

### Erro: "Module not specified" no IntelliJ
**Solução:** Recarregue o Maven
- Clique direito na raiz do projeto
- `Maven` → `Reload Projects`

### Erro: "JDK not configured"
**Solução:** Configure o SDK
- `IntelliJ IDEA` → `Preferences` → `Project Structure` → `Project`
- Selecione ou baixe Java 17+

---

## 💡 Dicas Importantes

1. **Primeira vez?** Recomendamos a Opção 2 (IDE) para entender melhor o código
2. **Quer ver logs?** Use `docker logs goc-app -f` para acompanhar em tempo real
3. **Debugar?** No IntelliJ, clique à esquerda de uma linha e use `Debug` (Shift+Ctrl+D)
4. **Banco vazio?** Execute `docker compose down -v` e depois `up -d` para resetar

---

## 📊 Endpoints

### POST /api/clientes
Cria um novo cliente no sistema.

**Request:**
```json
{
  "nome": "string (obrigatório, max 150)",
  "email": "string (obrigatório, único)",
  "endereco": "string (opcional)"
}
```

**Response:** HTTP 201 com dados do cliente criado

---

## 🔌 Conexão PostgreSQL

| Parâmetro | Valor |
|-----------|-------|
| Host | `localhost` |
| Porta | `5437` |
| Banco | `goc` |
| Usuário | `goc` |
| Senha | `goc_pass` |

---

## 📁 Estrutura do Projeto

```
goc/
├── src/
│   ├── main/
│   │   ├── java/br/com/goc/
│   │   │   ├── GocApplication.java
│   │   │   ├── model/
│   │   │   │   ├── Cliente.java
│   │   │   │   ├── Produto.java
│   │   │   │   ├── Orcamento.java
│   │   │   │   └── ItensOrcamento.java
│   │   │   ├── repository/
│   │   │   │   └── ClienteRepository.java
│   │   │   ├── service/
│   │   │   │   └── ClienteService.java
│   │   │   └── rest/
│   │   │       └── ClienteController.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       └── db/migration/
│   │           └── V1__create_cliente_table.sql
│   └── test/
│       └── java/br/com/goc/
│           ├── rest/
│           │   └── ClienteControllerTest.java
│           └── service/
│               └── ClienteServiceTest.java
├── docker-compose.yml
├── Dockerfile
├── init.sql
├── pom.xml
└── README.md
```

---

## 🗄️ Banco de Dados

Tabelas criadas automaticamente:
- `cliente` - Clientes do sistema
- `produto` - Produtos para orçamentos
- `orcamentos` - Orçamentos emitidos
- `itensorcamento` - Itens de cada orçamento

---

## 🛑 Parar a Aplicação

```bash
docker compose down
```

Para remover também os dados:
```bash
docker compose down -v
```

---

## 📝 Notas

- A aplicação usa Flyway para gerenciar migrations do banco de dados
- O schema é sincronizado automaticamente via Hibernate (ddl-auto: update)
- Os testes podem ser executados com: `./mvnw test`


