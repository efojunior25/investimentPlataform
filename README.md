💰 Investment Platform

Plataforma de gerenciamento de investimentos desenvolvida com Spring Boot 3.5 e Java 21

Mostrar Imagem
Mostrar Imagem
Mostrar Imagem
Mostrar Imagem
📋 Sobre o Projeto
Sistema completo de gerenciamento de investimentos que permite usuários cadastrarem suas carteiras, acompanharem ativos e visualizarem rentabilidade em tempo real.
🎯 Funcionalidades Principais

✅ Sistema de autenticação JWT
✅ Gerenciamento de usuários (CRUD completo)
✅ Controle de acesso baseado em roles (USER/ADMIN)
🚧 Carteira de investimentos (em desenvolvimento)
🚧 Registro de transações (em desenvolvimento)
🚧 Dashboard com métricas (em desenvolvimento)
🚧 Integração com APIs de cotações (em desenvolvimento)

🚀 Tecnologias Utilizadas
Backend

Java 21 - Linguagem de programação
Spring Boot 3.5.3 - Framework principal
Spring Security - Autenticação e autorização
Spring Data JPA - Persistência de dados
JWT (JSON Web Token) - Autenticação stateless
Lombok - Redução de boilerplate
Bean Validation - Validação de dados

Banco de Dados

MySQL 8.0 - Banco de dados relacional
Hibernate - ORM

DevOps

Docker - Containerização
Docker Compose - Orquestração de containers
Maven - Gerenciamento de dependências

📦 Pré-requisitos

Java 21 ou superior
Maven 3.9+
Docker e Docker Compose
Git

🔧 Instalação e Configuração
1. Clone o repositório
   bashgit clone https://github.com/seu-usuario/investimentPlataform.git
   cd investimentPlataform
2. Inicie o banco de dados
   bashdocker-compose up -d
3. Configure as variáveis de ambiente (opcional)
   Crie um arquivo .env na raiz do projeto:
   envDB_HOST=localhost
   DB_PORT=3306
   DB_NAME=inv_plat_db
   DB_USER=springuser
   DB_PASSWORD=ThePassword
   JWT_SECRET=sua_chave_secreta_aqui
   JWT_EXPIRATION=86400000
4. Execute a aplicação
   bash./mvnw spring-boot:run
   A aplicação estará disponível em: http://localhost:8080
   📚 Documentação

Guia de Uso da API
Instalação Detalhada
Guia Docker
Documentação do Banco de Dados

🔐 Autenticação
A API utiliza JWT (JSON Web Token) para autenticação. Veja o fluxo básico:
1. Cadastro
   httpPOST /v1/users
   Content-Type: application/json

{
"username": "João Silva",
"email": "joao@example.com",
"password": "senha123"
}
2. Login
   httpPOST /v1/auth/login
   Content-Type: application/json

{
"email": "joao@example.com",
"password": "senha123"
}
3. Usar o Token
   httpGET /v1/users/{userId}
   Authorization: Bearer {seu_token_jwt}
   Para mais detalhes, consulte a documentação completa da API.
   🗂️ Estrutura do Projeto
   src/
   ├── main/
   │   ├── java/com/mrxunim/investimentPlataform/
   │   │   ├── config/          # Configurações
   │   │   ├── controller/      # Controllers REST
   │   │   ├── dto/            # Data Transfer Objects
   │   │   ├── entity/         # Entidades JPA
   │   │   ├── repository/     # Repositórios
   │   │   ├── security/       # Segurança (JWT, Filters)
   │   │   └── service/        # Lógica de negócio
   │   └── resources/
   │       └── application.properties
   └── test/                    # Testes
   🧪 Executando os Testes
   bash# Executar todos os testes
   ./mvnw test

# Executar testes com cobertura
./mvnw test jacoco:report
📊 Dados de Teste
Para popular o banco com dados de teste:
bash# Criar usuário ADMIN
docker exec -i mysql_container mysql -u root -proot inv_plat_db < scripts/sql/seed/01_create_admin_user.sql
Credenciais do Admin:

Email: admin@example.com
Senha: admin123

🐳 Docker
Construir a imagem
bashdocker build -t investment-platform .
Executar com Docker Compose
bashdocker-compose up -d
Parar os containers
bashdocker-compose down
🛠️ Scripts Úteis
bash# Limpar e compilar
./mvnw clean install

# Executar em modo de desenvolvimento
./mvnw spring-boot:run

# Gerar JAR
./mvnw package

# Pular testes durante o build
./mvnw package -DskipTests
📈 Roadmap
Fase 1 - Autenticação ✅

Sistema de cadastro de usuários
Login com JWT
Roles e permissões
Validação de dados

Fase 2 - Tratamento de Erros 🚧

Exception handlers globais
Mensagens de erro padronizadas
Logs estruturados

Fase 3 - Documentação 🚧

Swagger/OpenAPI
Postman Collection
Guias de uso

Fase 4 - Core Business 📅

Entidades de investimento
Carteira de investimentos
Registro de transações
Cálculo de rentabilidade

Fase 5 - Integrações 📅

API de cotações em tempo real
Cache com Redis
Scheduled tasks

Fase 6 - DevOps 📅

CI/CD com GitHub Actions
Migrations com Flyway
Testes de integração
Deploy automatizado

🤝 Contribuindo
Contribuições são bem-vindas! Por favor:

Faça um Fork do projeto
Crie uma branch para sua feature (git checkout -b feature/MinhaFeature)
Commit suas mudanças (git commit -m 'Adiciona MinhaFeature')
Push para a branch (git push origin feature/MinhaFeature)
Abra um Pull Request

👨‍💻 Autor
Seu Nome

GitHub: @mrxunim
LinkedIn: Seu LinkedIn

📝 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
🙏 Agradecimentos

Spring Boot Documentation
Baeldung Tutorials
Stack Overflow Community


⭐ Se este projeto foi útil para você, considere dar uma estrela!