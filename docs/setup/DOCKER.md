🐳 Guia Docker - Investment Platform
Este guia explica como usar Docker para executar e desenvolver o projeto.
📋 Índice

Visão Geral
Docker Compose
Scripts Úteis
Dockerfile da Aplicação
Ambientes
Comandos Úteis


🎯 Visão Geral
O projeto utiliza Docker para:

Banco de Dados MySQL - Container isolado para desenvolvimento
Aplicação Spring Boot - Container da aplicação (futuro)
Redis - Cache (futuro)

Arquitetura de Containers
┌─────────────────────────────────────┐
│     Investment Platform API         │
│        (Spring Boot)                │
│         Port: 8080                  │
└──────────────┬──────────────────────┘
│
▼
┌─────────────────────────────────────┐
│          MySQL 8.0                  │
│         Port: 3306                  │
│       Volume: mysql_data            │
└─────────────────────────────────────┘

🐳 Docker Compose
Arquivo Atual: docker-compose.yml
yamlservices:
mysql:
image: mysql:8.0
ports:
- "3306:3306"
environment:
MYSQL_USER: springuser
MYSQL_PASSWORD: ThePassword
MYSQL_DATABASE: inv_plat_db
MYSQL_ROOT_PASSWORD: root
MYSQL_TCP_PORT: 3306
volumes:
- mysql_data:/var/lib/mysql
healthcheck:
test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
interval: 5s
timeout: 10s
retries: 5

volumes:
mysql_data:
Comandos Básicos
bash# Iniciar containers
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar containers
docker-compose down

# Reiniciar
docker-compose restart

# Reconstruir
docker-compose up -d --build

📜 Scripts Úteis
Crie estes scripts na pasta scripts/docker/ para facilitar o desenvolvimento:
scripts/docker/start.sh
bash#!/bin/bash

echo "🐳 Iniciando containers..."
docker-compose up -d

echo "⏳ Aguardando MySQL ficar pronto..."
sleep 10

echo "✅ Containers iniciados!"
docker-compose ps
scripts/docker/stop.sh
bash#!/bin/bash

echo "🛑 Parando containers..."
docker-compose down

echo "✅ Containers parados!"
scripts/docker/reset.sh
bash#!/bin/bash

echo "⚠️  ATENÇÃO: Isso vai apagar TODOS os dados!"
read -p "Tem certeza? (yes/no): " confirm

if [ "$confirm" == "yes" ]; then
echo "🗑️  Removendo containers e volumes..."
docker-compose down -v

    echo "🐳 Reiniciando do zero..."
    docker-compose up -d
    
    sleep 10
    
    echo "✅ Ambiente resetado!"
else
echo "❌ Cancelado."
fi
scripts/docker/logs.sh
bash#!/bin/bash

# Seguir logs do MySQL
docker-compose logs -f mysql
Tornar scripts executáveis
bashchmod +x scripts/docker/*.sh

📦 Dockerfile da Aplicação
Para criar um container da aplicação Spring Boot:
Dockerfile
dockerfile# Multi-stage build para otimização

# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar pom.xml e baixar dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fonte e compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar JAR da stage anterior
COPY --from=build /app/target/*.jar app.jar

# Expor porta
EXPOSE 8080

# Variáveis de ambiente
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Executar aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
Construir a Imagem
bashdocker build -t investment-platform:latest .

🌍 Docker Compose Completo
Para executar aplicação + banco de dados juntos:
docker-compose.full.yml
yamlversion: '3.8'

services:
mysql:
image: mysql:8.0
container_name: inv-mysql
ports:
- "3306:3306"
environment:
MYSQL_USER: springuser
MYSQL_PASSWORD: ThePassword
MYSQL_DATABASE: inv_plat_db
MYSQL_ROOT_PASSWORD: root
volumes:
- mysql_data:/var/lib/mysql
- ./scripts/sql/seed:/docker-entrypoint-initdb.d
healthcheck:
test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
interval: 5s
timeout: 10s
retries: 5
networks:
- inv-network

app:
build: .
container_name: inv-app
ports:
- "8080:8080"
environment:
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/inv_plat_db
SPRING_DATASOURCE_USERNAME: springuser
SPRING_DATASOURCE_PASSWORD: ThePassword
JWT_SECRET: ${JWT_SECRET}
JWT_EXPIRATION: 86400000
depends_on:
mysql:
condition: service_healthy
networks:
- inv-network

volumes:
mysql_data:

networks:
inv-network:
driver: bridge
Executar
bash# Usar o compose completo
docker-compose -f docker-compose.full.yml up -d

# Ver logs
docker-compose -f docker-compose.full.yml logs -f

# Parar
docker-compose -f docker-compose.full.yml down

🔧 Ambientes
Desenvolvimento
bash# Apenas MySQL local
docker-compose up -d

# App roda localmente via Maven
./mvnw spring-boot:run
Produção
bash# Tudo em containers
docker-compose -f docker-compose.full.yml up -d

💡 Comandos Úteis
Gerenciar Containers
bash# Listar containers rodando
docker ps

# Listar todos os containers
docker ps -a

# Ver logs
docker logs -f inv-mysql

# Executar comando no container
docker exec -it inv-mysql bash

# Parar container específico
docker stop inv-mysql

# Remover container
docker rm inv-mysql

# Remover imagem
docker rmi investment-platform:latest
Gerenciar Volumes
bash# Listar volumes
docker volume ls

# Inspecionar volume
docker volume inspect investimentplataform_mysql_data

# Remover volume (CUIDADO: apaga dados)
docker volume rm investimentplataform_mysql_data

# Limpar volumes não utilizados
docker volume prune
MySQL no Container
bash# Conectar ao MySQL
docker exec -it inv-mysql mysql -uspringuser -pThePassword inv_plat_db

# Executar SQL
docker exec -i inv-mysql mysql -uspringuser -pThePassword inv_plat_db < script.sql

# Backup
docker exec inv-mysql mysqldump -uspringuser -pThePassword inv_plat_db > backup.sql

# Restore
docker exec -i inv-mysql mysql -uspringuser -pThePassword inv_plat_db < backup.sql
Limpeza
bash# Remover tudo (containers, volumes, redes)
docker-compose down -v

# Limpar sistema
docker system prune -a

# Limpar apenas containers parados
docker container prune

# Limpar apenas imagens não utilizadas
docker image prune

🐛 Troubleshooting
Container não inicia
bash# Ver logs detalhados
docker logs inv-mysql

# Verificar status
docker ps -a

# Inspecionar container
docker inspect inv-mysql
Porta já em uso
bash# Descobrir processo usando a porta
# Linux/macOS
lsof -i :3306

# Windows
netstat -ano | findstr :3306

# Matar processo
kill -9 <PID>
Volume com problemas
bash# Remover volume e recriar
docker-compose down -v
docker volume rm investimentplataform_mysql_data
docker-compose up -d
Aplicação não conecta ao MySQL
bash# Verificar rede
docker network ls
docker network inspect investimentplataform_default

# Testar conectividade
docker exec inv-app ping mysql
Baixa performance
bash# Alocar mais recursos no Docker Desktop
# Settings → Resources → Advanced
# Aumentar CPU e Memory

# Ou via docker-compose
services:
app:
deploy:
resources:
limits:
cpus: '2'
memory: 2G

📊 Monitoramento
Ver uso de recursos
bash# Estatísticas em tempo real
docker stats

# Uso de espaço
docker system df
Healthchecks
bash# Ver status de saúde
docker ps --format "table {{.Names}}\t{{.Status}}"

# Testar healthcheck manualmente
docker exec inv-mysql mysqladmin ping -h localhost

🚀 Boas Práticas

Use .dockerignore para evitar copiar arquivos desnecessários
Multi-stage builds para reduzir tamanho da imagem
Healthchecks para garantir disponibilidade
Volumes nomeados para persistência de dados
Redes customizadas para isolamento
Variáveis de ambiente para configuração
Logs estruturados para debugging


📚 Recursos Adicionais

Docker Documentation
Docker Compose Reference
Best Practices


Última atualização: Janeiro 2025