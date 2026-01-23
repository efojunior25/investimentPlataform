📖 Guia do Swagger UI - Investment Platform
🎯 O que é Swagger UI?
Swagger UI é uma interface interativa que permite:

📖 Visualizar toda a documentação da API
🧪 Testar endpoints diretamente no navegador
📋 Ver exemplos de request/response
🔐 Autenticar com JWT
📝 Validar schemas automaticamente


🚀 Acessando o Swagger UI
URLs Disponíveis
# Swagger UI (Interface Visual)
http://localhost:8080/swagger-ui.html

# API Docs (JSON)
http://localhost:8080/api-docs

# API Docs (YAML)
http://localhost:8080/api-docs.yaml

🔐 Como Autenticar no Swagger
Passo a Passo

Acesse o Swagger UI

http://localhost:8080/swagger-ui.html

Cadastre um usuário (se ainda não tiver)

Vá até Usuários → POST /v1/users
Clique em Try it out
Preencha o JSON:



json     {
"username": "Test User",
"email": "test@example.com",
"password": "Test123"
}

Clique em Execute


Faça login para obter o token

Vá até Autenticação → POST /v1/auth/login
Clique em Try it out
Preencha:



json     {
"email": "test@example.com",
"password": "Test123"
}

Clique em Execute
Copie o token da resposta


Autenticar no Swagger

Clique no botão Authorize 🔓 (topo direito)
Cole o token JWT no campo Value
Clique em Authorize
Clique em Close


Agora você está autenticado! 🎉

Todos os endpoints protegidos estão disponíveis
O token será enviado automaticamente nos headers




🧪 Testando Endpoints
Exemplo: Buscar Usuário por ID

Vá até GET /v1/users/{userId}
Clique em "Try it out"
Preencha o userId (use o UUID retornado no cadastro)
Clique em "Execute"
Veja a resposta abaixo

Exemplo: Atualizar Usuário

Vá até PUT /v1/users/{userId}
Clique em "Try it out"
Preencha o userId
Edite o JSON com os dados que quer atualizar:

json   {
"username": "Novo Nome"
}

Clique em "Execute"


📋 Recursos do Swagger UI
1. Schemas
   No topo da página, clique em Schemas para ver todos os modelos de dados:

CreateUserDTO
LoginRequestDTO
UserResponseDTO
ProblemDetail
etc.

2. Try it out
   Permite testar o endpoint diretamente:

Editar o JSON de request
Ver o comando curl gerado
Executar e ver a resposta

3. Responses
   Mostra todos os códigos de resposta possíveis:

✅ 200/201 - Sucesso
❌ 400 - Validação
❌ 401 - Não autenticado
❌ 403 - Sem permissão
❌ 404 - Não encontrado
❌ 409 - Conflito

4. Request Samples
   Exemplos prontos de requisição que você pode usar diretamente.
5. cURL
   Cada endpoint mostra o comando curl equivalente que você pode copiar e usar no terminal.

🎨 Estrutura da Documentação
Tags (Grupos)
Os endpoints estão organizados em grupos:

🔐 Autenticação - Login e tokens
👥 Usuários - CRUD de usuários

Informações de Cada Endpoint
Cada endpoint mostra:

Método HTTP (GET, POST, PUT, DELETE)
Path (caminho do endpoint)
Descrição detalhada
Parâmetros necessários
Request body (se aplicável)
Responses possíveis
Schemas dos objetos


🔍 Exemplos Práticos
Fluxo Completo de Uso
1. POST /v1/users
   → Cadastrar novo usuário

2. POST /v1/auth/login
   → Fazer login e obter token

3. Authorize (cadeado)
   → Colar o token JWT

4. GET /v1/users/{userId}
   → Buscar seus dados

5. PUT /v1/users/{userId}
   → Atualizar seu perfil

6. GET /v1/users (se for ADMIN)
   → Listar todos os usuários
   Testando Erros
   Validação (400):
   jsonPOST /v1/users
   {
   "username": "AB",
   "email": "invalido",
   "password": "123"
   }
   Email duplicado (409):
   jsonPOST /v1/users
   {
   "username": "Outro Nome",
   "email": "email_ja_existente@example.com",
   "password": "Senha123"
   }
   Não autenticado (401):

Remova o token (Authorize → Logout)
Tente GET /v1/users/{userId}

Sem permissão (403):

Como usuário comum, tente GET /v1/users


💡 Dicas Úteis
1. Limpando o Token
   Authorize → Logout → Close
2. Copiando Request
   Após executar, você pode copiar:

Request URL - URL completa
Curl - Comando curl
Request body - JSON enviado

3. Baixando Spec
# OpenAPI JSON
http://localhost:8080/api-docs

# OpenAPI YAML
http://localhost:8080/api-docs.yaml
4. Importando no Postman

Baixe http://localhost:8080/api-docs
Abra Postman
Import → OpenAPI 3.0
Cole o JSON


⚙️ Configurações Disponíveis
application.properties
properties# Customizar paths
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Ordenação
springdoc.swagger-ui.operations-sorter=alpha
springdoc.swagger-ui.tags-sorter=alpha

# Try it out habilitado por padrão
springdoc.swagger-ui.try-it-out-enabled=true

# Filtro de busca
springdoc.swagger-ui.filter=true

🚫 Troubleshooting
Swagger não abre
Verifique:
bash# Aplicação rodando?
curl http://localhost:8080/actuator/health

# Swagger acessível?
curl http://localhost:8080/swagger-ui.html
Solução: Verifique se adicionou as URLs no SecurityConfig
Token não funciona
Verifique:

Token copiado completamente (sem espaços)
Token não expirado (24h)
Formato: apenas o token, sem "Bearer"

Como testar o token:
bashTOKEN="seu_token_aqui"
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/v1/users/{userId}
Endpoint não aparece
Verifique:

Anotação @RestController na classe
Anotação @RequestMapping ou equivalente no método
Aplicação reiniciada após mudanças


📸 Screenshots
Interface Principal
Mostrar Imagem
Teste de Endpoint
Mostrar Imagem
Autenticação
Mostrar Imagem

🔗 Links Úteis

Swagger UI Oficial
OpenAPI Specification
SpringDoc Documentation


📝 Changelog
v1.0.0 - 2024-01-20

✅ Documentação completa de todos endpoints
✅ Autenticação JWT integrada
✅ Exemplos de request/response
✅ Schemas detalhados
✅ Suporte a RFC 7807


Última atualização: Janeiro 2025