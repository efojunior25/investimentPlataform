🔧 Guia de Instalação - Investment Platform
Este guia detalha passo a passo como configurar o ambiente de desenvolvimento.
📋 Índice

Requisitos do Sistema
Instalação do Java
Instalação do Maven
Instalação do Docker
Configuração do Projeto
Inicialização
Verificação
Troubleshooting


📦 Requisitos do Sistema
Mínimos

Sistema Operacional: Windows 10+, macOS 10.14+, ou Linux
RAM: 4 GB (8 GB recomendado)
Espaço em Disco: 2 GB

Software Necessário

Java Development Kit (JDK) 21+
Maven 3.9+
Docker 24.0+
Docker Compose 2.0+
Git 2.30+


☕ Instalação do Java
Windows

Baixe o JDK 21:

Acesse: https://adoptium.net/
Baixe o instalador Windows x64


Instale:

Execute o instalador
Marque "Add to PATH"


Verifique:

cmdjava -version
macOS
bash# Usando Homebrew
brew install openjdk@21

# Adicionar ao PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verificar
java -version
Linux (Ubuntu/Debian)
bash# Instalar JDK 21
sudo apt update
sudo apt install openjdk-21-jdk

# Verificar
java -version

📦 Instalação do Maven
Windows

Baixe:

https://maven.apache.org/download.cgi
Escolha o .zip binário


Extraia:

Extraia para C:\Program Files\Apache\maven


Configure PATH:

Painel de Controle → Sistema → Variáveis de Ambiente
Adicione C:\Program Files\Apache\maven\bin ao PATH


Verifique:

cmdmvn -version
macOS
bashbrew install maven
mvn -version
Linux
bashsudo apt update
sudo apt install maven
mvn -version

🐳 Instalação do Docker
Windows

Baixe Docker Desktop:

https://www.docker.com/products/docker-desktop


Instale:

Execute o instalador
Reinicie o computador


Verifique:

cmddocker --version
docker-compose --version
macOS
bash# Baixe Docker Desktop para Mac
# https://www.docker.com/products/docker-desktop

# Ou usando Homebrew
brew install --cask docker

# Verifique
docker --version
docker-compose --version
Linux
bash# Ubuntu/Debian
sudo apt update
sudo apt install docker.io docker-compose

# Adicionar usuário ao grupo docker
sudo usermod -aG docker $USER
newgrp docker

# Verifique
docker --version
docker-compose --version

🚀 Configuração do Projeto
1. Clone o Repositório
   bashgit clone https://github.com/seu-usuario/investimentPlataform.git
   cd investimentPlataform
2. Crie as Pastas Necessárias
   bash# Windows (PowerShell)
   New-Item -ItemType Directory -Force -Path docs/api, docs/api/postman, docs/database, docs/setup, scripts/sql/seed, scripts/sql/maintenance, scripts/docker, scripts/deploy, src/main/resources/db/migration

# Linux/macOS
mkdir -p docs/{api/postman,database,setup} scripts/{sql/{seed,maintenance},docker,deploy} src/main/resources/db/migration
3. Configure o application.properties
   Edite src/main/resources/application.properties:
   propertiesspring.application.name=investimentPlataform

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/inv_plat_db?createDatabaseIfNotExist=true
spring.datasource.username=springuser
spring.datasource.password=ThePassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT - ALTERE ESTA CHAVE EM PRODUÇÃO!
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970337336763979244226452948404D635166546A576E5A7234753778214125442A
jwt.expiration=86400000
4. Crie o Script de Criação do Admin
   Crie o arquivo scripts/sql/seed/01_create_admin_user.sql:
   sqlINSERT INTO tb_users (user_id, username, email, password, role, created_at, updated_at)
   VALUES (
   UUID(),
   'Administrator',
   'admin@example.com',
   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
   'ADMIN',
   NOW(),
   NOW()
   );

▶️ Inicialização
1. Inicie o Banco de Dados
   bashdocker-compose up -d
   Aguarde alguns segundos para o MySQL inicializar completamente.
2. Compile o Projeto
   bash./mvnw clean install
3. Execute a Aplicação
   bash./mvnw spring-boot:run
   A aplicação estará disponível em: http://localhost:8080
4. (Opcional) Crie o Usuário Admin
   bash# Windows (PowerShell)
   Get-Content scripts/sql/seed/01_create_admin_user.sql | docker exec -i investimentplataform-mysql-1 mysql -uspringuser -pThePassword inv_plat_db

# Linux/macOS
docker exec -i investimentplataform-mysql-1 mysql -uspringuser -pThePassword inv_plat_db < scripts/sql/seed/01_create_admin_user.sql

✅ Verificação
1. Verifique a Saúde da Aplicação
   bashcurl http://localhost:8080/actuator/health
2. Teste o Cadastro de Usuário
   bashcurl -X POST http://localhost:8080/v1/users \
   -H "Content-Type: application/json" \
   -d '{
   "username": "Teste",
   "email": "teste@example.com",
   "password": "senha123"
   }'
   Deve retornar 201 Created.
3. Teste o Login
   bashcurl -X POST http://localhost:8080/v1/auth/login \
   -H "Content-Type: application/json" \
   -d '{
   "email": "teste@example.com",
   "password": "senha123"
   }'
   Deve retornar um objeto JSON com o token JWT.
4. Verifique o Banco de Dados
   bash# Conectar ao MySQL
   docker exec -it investimentplataform-mysql-1 mysql -uspringuser -pThePassword inv_plat_db

# Dentro do MySQL
SELECT * FROM tb_users;

🐛 Troubleshooting
Problema: "Port 3306 already in use"
Solução:
bash# Pare o MySQL local
sudo service mysql stop  # Linux
brew services stop mysql  # macOS

# Ou mude a porta no docker-compose.yml
ports:
- "3307:3306"  # Usa porta 3307 no host
  Problema: "Cannot connect to database"
  Solução:
  bash# Verifique se o container está rodando
  docker ps

# Verifique os logs
docker logs investimentplataform-mysql-1

# Reinicie o container
docker-compose restart mysql
Problema: "Java version not found"
Solução:
bash# Verifique a versão do Java
java -version

# Se for menor que 21, atualize conforme instruções acima
Problema: "Maven command not found"
Solução:
bash# Use o Maven Wrapper incluído no projeto
./mvnw clean install  # Linux/macOS
.\mvnw.cmd clean install  # Windows
Problema: "Port 8080 already in use"
Solução:
Edite application.properties e adicione:
propertiesserver.port=8081
Problema: JWT Token Inválido
Solução:

Verifique se o token não expirou (24h)
Certifique-se de usar o formato: Authorization: Bearer {token}
Gere uma nova chave JWT:

bashopenssl rand -base64 64

🔄 Comandos Úteis
Docker
bash# Ver containers rodando
docker ps

# Ver logs da aplicação
docker logs -f investimentplataform-mysql-1

# Parar todos os containers
docker-compose down

# Limpar volumes (CUIDADO: apaga dados)
docker-compose down -v

# Reconstruir containers
docker-compose up -d --build
Maven
bash# Limpar projeto
./mvnw clean

# Compilar
./mvnw compile

# Executar testes
./mvnw test

# Gerar JAR
./mvnw package

# Pular testes
./mvnw package -DskipTests
Git
bash# Clonar
git clone <url>

# Ver status
git status

# Criar branch
git checkout -b feature/minha-feature

# Commit
git add .
git commit -m "Mensagem"

# Push
git push origin feature/minha-feature

🎓 Próximos Passos
Após a instalação bem-sucedida:

✅ Leia a Documentação da API
✅ Explore os endpoints com Postman
✅ Configure seu IDE (IntelliJ IDEA, VS Code, Eclipse)
✅ Contribua com o projeto!


📞 Suporte
Se encontrar problemas não listados aqui:

Verifique as Issues do GitHub
Abra uma nova Issue descrevendo o problema
Inclua logs e detalhes do ambiente


Última atualização: Janeiro 2025