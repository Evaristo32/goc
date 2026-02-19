# GOC — Guia de Setup, Compilação e Deploy

## 📋 Pré-requisitos

### **Desenvolvimento Local**
- ✅ Docker (versão 20.10+)
- ✅ Docker Compose (versão 2.0+)
- ✅ Maven 3.9+ (opcional, se quiser compilar localmente sem Docker)
- ✅ Java 17 (opcional, se quiser rodar local sem Docker)
- ✅ Git

### **Verificar Instalações**
```bash
docker --version          # Docker version 20.10.0 or higher
docker compose version    # Docker Compose version 2.0.0 or higher
mvn --version            # Apache Maven 3.9.0 or higher (opcional)
java -version            # openjdk 17.0.x or higher (opcional)
```

---

## 🚀 Quick Start (Recomendado)

A forma mais rápida de subir o projeto é usar o script de setup:

```bash
cd /Users/evaristodev/desenvolvimento/projetos/pessoal/goc
chmod +x setup.sh
./setup.sh
```

**O que este script faz:**
1. ✅ Compila o projeto Maven
2. ✅ Constrói a imagem Docker
3. ✅ Remove containers e volumes antigos
4. ✅ Sobe os containers (PostgreSQL + Spring Boot)
5. ✅ Executa script de inicialização do banco
6. ✅ Exibe status final com tabelas criadas

**Tempo estimado:** 2-3 minutos

---

## 🛠️ Instalação Passo a Passo

### **1. Clonar ou Acessar o Repositório**

```bash
cd /Users/evaristodev/desenvolvimento/projetos/pessoal/goc
```

### **2. Compilar o Projeto (Opcional)**

Se você quiser compilar localmente (requer Java 17 + Maven instalados):

```bash
./mvnw clean package -DskipTests
```

**Saída esperada:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 1.8 s
```

*Nota: Isso é opcional. O Docker Compose pode compilar automaticamente na primeira execução.*

### **3. Construir a Imagem Docker**

```bash
docker build -t goc:latest .
```

**Saída esperada:**
```
=> naming to docker.io/library/goc:latest done
```

### **4. Iniciar os Containers**

```bash
docker compose up -d
```

**Saída esperada:**
```
✔ Network goc_goc-network Created
✔ Container goc-postgres Healthy
✔ Container goc-app Created
```

### **5. Aguardar a Inicialização**

Aguarde 20-30 segundos para o PostgreSQL estar pronto e a aplicação iniciar.

### **6. Criar as Tabelas do Banco**

```bash
docker cp init.sql goc-postgres:/init.sql
docker exec goc-postgres psql -U goc -d goc -f /init.sql
```

**Saída esperada:**
```
CREATE SEQUENCE
CREATE TABLE
...
Todas as tabelas foram criadas com sucesso!
```

### **7. Verificar o Status**

```bash
docker compose ps
```

**Saída esperada:**
```
NAME           IMAGE           STATUS                   PORTS
goc-postgres   postgres:15     Up (healthy)             0.0.0.0:5437->5432/tcp
goc-app        goc:latest      Up                       0.0.0.0:8080->8080/tcp
```

---

## ✅ Verificações de Sucesso

### **Health Check da Aplicação**

```bash
curl http://localhost:8080/actuator/health
```

**Resposta esperada:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    }
  }
}
```

### **Verificar Tabelas do Banco**

```bash
docker exec goc-postgres psql -U goc -d goc -c "SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename;"
```

**Saída esperada:**
```
    tablename    
-----------
 cliente
 itensorcamento
 orcamentos
 produto
(4 rows)
```

### **Verificar Logs da Aplicação**

```bash
docker compose logs goc-app
```

Procure por:
- ✅ "Started GocApplication"
- ✅ "HikariPool-1 - Start completed"
- ✅ "Hibernate initialization"

---

## 🔄 Comandos Úteis

### **Visualizar Logs em Tempo Real**

```bash
docker compose logs -f goc-app
```

Pressione `Ctrl+C` para sair.

### **Visualizar Logs do PostgreSQL**

```bash
docker compose logs -f goc-postgres
```

### **Conectar ao Banco de Dados Diretamente**

```bash
docker exec -it goc-postgres psql -U goc -d goc
```

Dentro do psql:
```sql
\dt                    -- listar tabelas
\d cliente             -- estrutura da tabela
SELECT * FROM cliente; -- listar registros
\q                     -- sair
```

### **Parar os Containers**

```bash
docker compose stop
```

### **Remover Containers e Volumes**

```bash
docker compose down -v
```

⚠️ **Aviso**: Isso remove todos os dados do banco!

### **Reiniciar Containers**

```bash
docker compose restart
```

### **Acessar o Container da App**

```bash
docker exec -it goc-app bash
```

### **Acessar o Container do PostgreSQL**

```bash
docker exec -it goc-postgres bash
```

---

## 🚀 Deploy em Produção

### **1. Variáveis de Ambiente**

Crie um arquivo `.env` na raiz do projeto:

```env
POSTGRES_DB=goc_prod
POSTGRES_USER=goc_user
POSTGRES_PASSWORD=seu_senha_segura_aqui_123
DB_HOST=db
DB_PORT=5432
JAVA_OPTS=-Xms512m -Xmx2g
```

### **2. Usar o .env no Docker Compose**

O docker-compose.yml já referencia o `.env`:

```yaml
environment:
  POSTGRES_DB: ${POSTGRES_DB:-goc}
  POSTGRES_USER: ${POSTGRES_USER:-goc}
  POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-goc_pass}
```

### **3. Compilar e Deploy**

```bash
# Compilar com configurações de produção
./mvnw clean package -DskipTests -Dspring.profiles.active=prod

# Fazer build da imagem
docker build -t goc:v1.0 .

# Push para registry (se usar)
docker tag goc:v1.0 seu-registry/goc:v1.0
docker push seu-registry/goc:v1.0

# Deploy com docker compose
docker compose -f docker-compose.prod.yml up -d
```

### **4. Recomendações de Segurança**

- 🔒 Use senhas fortes (mínimo 20 caracteres)
- 🔒 Ative HTTPS/TLS no reverse proxy (Nginx/Traefik)
- 🔒 Configure firewall para permitir apenas tráfego necessário
- 🔒 Mantenha Docker e PostgreSQL atualizados
- 🔒 Use Docker secrets para credenciais sensíveis
- 🔒 Implemente monitoramento e alertas (Prometheus/Grafana)
- 🔒 Faça backup regular do banco de dados
- 🔒 Configure logs centralizados (ELK Stack/Splunk)

---

## 🐛 Troubleshooting

### **Problema: "Connection refused" ao conectar ao banco**

**Solução:**
```bash
# Verificar se PostgreSQL está saudável
docker compose ps

# Ver logs do PostgreSQL
docker compose logs goc-postgres

# Reiniciar PostgreSQL
docker compose restart goc-postgres
docker compose logs goc-postgres
```

### **Problema: "Port already in use"**

**Solução:**
```bash
# Encontrar processo usando a porta
lsof -i :8080
lsof -i :5437

# Matar processo (substitua PID)
kill -9 <PID>

# Ou mudar a porta em docker-compose.yml
# Altere "8080:8080" para "8081:8080"
```

### **Problema: Tabelas não foram criadas**

**Solução:**
```bash
# Executar init.sql manualmente
docker cp init.sql goc-postgres:/init.sql
docker exec goc-postgres psql -U goc -d goc -f /init.sql

# Ou verificar logs
docker compose logs goc-app | grep -i "error\|exception"
```

### **Problema: Imagem Docker não atualiza**

**Solução:**
```bash
# Remover imagem antiga
docker rmi goc:latest

# Fazer rebuild
docker build -t goc:latest .

# Subir novamente
docker compose up -d --force-recreate
```

### **Problema: "Out of Memory" na aplicação**

**Solução:**

Edite `docker-compose.yml`:
```yaml
environment:
  JAVA_OPTS: "-Xms512m -Xmx2g"  # Aumentar valores
```

Depois:
```bash
docker compose down
docker compose up -d
```

---

## 📊 Monitoramento

### **Verificar Uso de Memória**

```bash
docker stats goc-app goc-postgres
```

### **Verificar Tamanho do Banco**

```bash
docker exec goc-postgres psql -U goc -d goc -c "SELECT pg_size_pretty(pg_database_size('goc'));"
```

### **Fazer Backup do Banco**

```bash
docker exec goc-postgres pg_dump -U goc -d goc > backup_$(date +%Y%m%d_%H%M%S).sql
```

### **Restaurar do Backup**

```bash
docker exec -i goc-postgres psql -U goc -d goc < backup_20260219_123456.sql
```

---

## 📈 Performance e Otimizações

### **Ativar Slow Query Log**

Adicione ao `docker-compose.yml`:
```yaml
db:
  command: postgres -c log_min_duration_statement=1000
```

### **Aumentar Pool de Conexões**

Edite `application.yaml`:
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

### **Aumentar Cache Hibernate**

Edite `application.yaml`:
```yaml
spring:
  jpa:
    properties:
      hibernate:
        cache:
          use_second_level_cache: true
          region:
            factory_class: org.hibernate.cache.jcache.JCacheRegionFactory
```

---

## 🔄 CI/CD (GitHub Actions)

Exemplo de workflow para automatic build e push:

```yaml
# .github/workflows/docker.yml
name: Build and Push Docker Image

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Build image
        run: docker build -t goc:latest .
      - name: Push to registry
        env:
          REGISTRY_TOKEN: ${{ secrets.REGISTRY_TOKEN }}
        run: |
          echo $REGISTRY_TOKEN | docker login -u user --password-stdin
          docker tag goc:latest registry/goc:latest
          docker push registry/goc:latest
```

---

## 📝 Checklist de Deploy

- [ ] Compilar projeto: `./mvnw clean package -DskipTests`
- [ ] Testar localmente: `./setup.sh`
- [ ] Atualizar variáveis de ambiente em `.env`
- [ ] Atualizar versão em `pom.xml`
- [ ] Fazer commit e tag: `git tag v1.0.0`
- [ ] Fazer push: `git push && git push --tags`
- [ ] Build imagem Docker: `docker build -t goc:v1.0 .`
- [ ] Push para registry: `docker push registry/goc:v1.0`
- [ ] Deploy em staging e testar
- [ ] Deploy em produção
- [ ] Testar health checks: `curl https://sua-app.com/actuator/health`
- [ ] Verificar logs: `docker compose logs goc-app`
- [ ] Documentar mudanças no CHANGELOG

---

## 🆘 Suporte

Para problemas ou dúvidas:

1. Verificar logs: `docker compose logs -f`
2. Consultar ARCHITECTURE.md para entender a arquitetura
3. Verificar `.env` para configurações
4. Reconstruir tudo do zero:
   ```bash
   docker compose down -v
   rm -rf target/
   ./setup.sh
   ```

---

**Última atualização:** Fevereiro 2026  
**Versão:** 1.0.0

