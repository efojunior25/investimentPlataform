🚨 Tratamento de Erros - Investment Platform API
📋 Índice

Formato de Resposta de Erro
Códigos de Status HTTP
Tipos de Erros
Exemplos de Erros


📦 Formato de Resposta de Erro
Todas as respostas de erro seguem o mesmo padrão:
json{
"status": 400,
"error": "Bad Request",
"message": "Mensagem de erro amigável",
"path": "/v1/users",
"timestamp": "2024-01-20T10:30:00",
"validationErrors": [
{
"field": "email",
"message": "O email deve ser válido",
"rejectedValue": "email-invalido"
}
]
}
Campos da Resposta
CampoTipoDescriçãostatusnumberCódigo HTTP do erroerrorstringNome do erro HTTPmessagestringMensagem descritiva do erropathstringEndpoint que gerou o errotimestampdatetimeData e hora do errovalidationErrorsarrayLista de erros de validação (opcional)

📊 Códigos de Status HTTP
2xx - Sucesso
CódigoNomeUso200OKRequisição bem-sucedida201CreatedRecurso criado com sucesso204No ContentOperação bem-sucedida sem retorno
4xx - Erros do Cliente
CódigoNomeUso400Bad RequestDados inválidos ou erro de validação401UnauthorizedNão autenticado ou token inválido403ForbiddenSem permissão para acessar404Not FoundRecurso não encontrado409ConflictConflito (ex: email duplicado)
5xx - Erros do Servidor
CódigoNomeUso500Internal Server ErrorErro interno não tratado

🎯 Tipos de Erros
1. Erro de Validação (400)
   Ocorre quando os dados enviados não passam na validação.
   Exemplo de Request:
   jsonPOST /v1/users
   {
   "username": "AB",
   "email": "email-invalido",
   "password": "123"
   }
   Resposta:
   json{
   "status": 400,
   "error": "Validation Error",
   "message": "Erro de validação nos dados enviados",
   "path": "/v1/users",
   "timestamp": "2024-01-20T10:30:00",
   "validationErrors": [
   {
   "field": "username",
   "message": "O nome de usuário deve ter entre 3 e 25 caracteres",
   "rejectedValue": "AB"
   },
   {
   "field": "email",
   "message": "O email deve ser válido",
   "rejectedValue": "email-invalido"
   },
   {
   "field": "password",
   "message": "A senha deve ter entre 6 e 100 caracteres",
   "rejectedValue": "123"
   },
   {
   "field": "password",
   "message": "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número",
   "rejectedValue": "123"
   }
   ]
   }
2. Recurso Não Encontrado (404)
   Ocorre quando o recurso solicitado não existe.
   Exemplo de Request:
   httpGET /v1/users/550e8400-e29b-41d4-a716-446655440000
   Resposta:
   json{
   "status": 404,
   "error": "Not Found",
   "message": "Usuário não encontrado com userId: '550e8400-e29b-41d4-a716-446655440000'",
   "path": "/v1/users/550e8400-e29b-41d4-a716-446655440000",
   "timestamp": "2024-01-20T10:30:00"
   }
3. Recurso Duplicado (409)
   Ocorre quando tenta criar um recurso que já existe.
   Exemplo de Request:
   jsonPOST /v1/users
   {
   "username": "João Silva",
   "email": "joao@example.com",
   "password": "Senha123"
   }
   Resposta (se email já existe):
   json{
   "status": 409,
   "error": "Conflict",
   "message": "Usuário já existe com email: 'joao@example.com'",
   "path": "/v1/users",
   "timestamp": "2024-01-20T10:30:00"
   }
4. Não Autorizado (401)
   Ocorre quando as credenciais são inválidas ou o token está ausente/inválido.
   Exemplo de Request (credenciais inválidas):
   jsonPOST /v1/auth/login
   {
   "email": "joao@example.com",
   "password": "senhaerrada"
   }
   Resposta:
   json{
   "status": 401,
   "error": "Unauthorized",
   "message": "Email ou senha inválidos",
   "path": "/v1/auth/login",
   "timestamp": "2024-01-20T10:30:00"
   }
   Exemplo de Request (sem token):
   httpGET /v1/users/550e8400-e29b-41d4-a716-446655440000
   Resposta:
   json{
   "status": 401,
   "error": "Unauthorized",
   "message": "Token JWT ausente ou inválido",
   "path": "/v1/users/550e8400-e29b-41d4-a716-446655440000",
   "timestamp": "2024-01-20T10:30:00"
   }
5. Acesso Negado (403)
   Ocorre quando o usuário não tem permissão para acessar o recurso.
   Exemplo de Request:
   httpGET /v1/users
   Authorization: Bearer {token_de_usuario_comum}
   Resposta:
   json{
   "status": 403,
   "error": "Forbidden",
   "message": "Você não tem permissão para acessar este recurso",
   "path": "/v1/users",
   "timestamp": "2024-01-20T10:30:00"
   }
6. Erro Interno do Servidor (500)
   Ocorre quando há um erro não tratado no servidor.
   Resposta:
   json{
   "status": 500,
   "error": "Internal Server Error",
   "message": "Ocorreu um erro interno no servidor",
   "path": "/v1/users",
   "timestamp": "2024-01-20T10:30:00"
   }

🔍 Exemplos Completos por Endpoint
POST /v1/users (Cadastro)
✅ Sucesso
json{
"success": true,
"message": "Usuário criado com sucesso",
"data": {
"userId": "550e8400-e29b-41d4-a716-446655440000",
"username": "João Silva",
"email": "joao@example.com",
"role": "USER",
"createdAt": "2024-01-20T10:30:00Z",
"updatedAt": "2024-01-20T10:30:00Z"
},
"timestamp": "2024-01-20T10:30:00"
}
❌ Erro: Email já cadastrado
json{
"status": 409,
"error": "Conflict",
"message": "Usuário já existe com email: 'joao@example.com'",
"path": "/v1/users",
"timestamp": "2024-01-20T10:30:00"
}
❌ Erro: Dados inválidos
json{
"status": 400,
"error": "Validation Error",
"message": "Erro de validação nos dados enviados",
"path": "/v1/users",
"timestamp": "2024-01-20T10:30:00",
"validationErrors": [
{
"field": "password",
"message": "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número",
"rejectedValue": "senha123"
}
]
}
POST /v1/auth/login
✅ Sucesso
json{
"success": true,
"message": "Login realizado com sucesso",
"data": {
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
"type": "Bearer",
"expiresIn": 86400000
},
"timestamp": "2024-01-20T10:30:00"
}
❌ Erro: Credenciais inválidas
json{
"status": 401,
"error": "Unauthorized",
"message": "Email ou senha inválidos",
"path": "/v1/auth/login",
"timestamp": "2024-01-20T10:30:00"
}
GET /v1/users/{userId}
✅ Sucesso
json{
"success": true,
"data": {
"userId": "550e8400-e29b-41d4-a716-446655440000",
"username": "João Silva",
"email": "joao@example.com",
"role": "USER",
"createdAt": "2024-01-20T10:30:00Z",
"updatedAt": "2024-01-20T10:30:00Z"
},
"timestamp": "2024-01-20T10:30:00"
}
❌ Erro: Usuário não encontrado
json{
"status": 404,
"error": "Not Found",
"message": "Usuário não encontrado com userId: '550e8400-e29b-41d4-a716-446655440000'",
"path": "/v1/users/550e8400-e29b-41d4-a716-446655440000",
"timestamp": "2024-01-20T10:30:00"
}
❌ Erro: Sem permissão
json{
"status": 403,
"error": "Forbidden",
"message": "Você não tem permissão para visualizar este perfil",
"path": "/v1/users/550e8400-e29b-41d4-a716-446655440000",
"timestamp": "2024-01-20T10:30:00"
}

🛠️ Tratando Erros no Frontend
JavaScript/TypeScript
javascriptasync function createUser(userData) {
try {
const response = await fetch('http://localhost:8080/v1/users', {
method: 'POST',
headers: {
'Content-Type': 'application/json'
},
body: JSON.stringify(userData)
});

    const data = await response.json();

    if (!response.ok) {
      // Erro de validação
      if (data.validationErrors) {
        data.validationErrors.forEach(error => {
          console.error(`${error.field}: ${error.message}`);
        });
      } else {
        console.error(data.message);
      }
      throw new Error(data.message);
    }

    return data;
} catch (error) {
console.error('Erro ao criar usuário:', error);
throw error;
}
}
Axios
javascriptimport axios from 'axios';

axios.interceptors.response.use(
response => response,
error => {
if (error.response) {
const { status, data } = error.response;

      switch (status) {
        case 400:
          // Erro de validação
          if (data.validationErrors) {
            data.validationErrors.forEach(err => {
              console.error(`${err.field}: ${err.message}`);
            });
          }
          break;
        case 401:
          // Redirecionar para login
          window.location.href = '/login';
          break;
        case 403:
          alert('Você não tem permissão para acessar este recurso');
          break;
        case 404:
          alert('Recurso não encontrado');
          break;
        case 409:
          alert(data.message);
          break;
        case 500:
          alert('Erro no servidor. Tente novamente mais tarde.');
          break;
      }
    }
    return Promise.reject(error);
}
);

📝 Regras de Validação
Username

✅ Obrigatório
✅ Entre 3 e 25 caracteres
✅ Apenas letras e espaços

Email

✅ Obrigatório
✅ Formato válido de email
✅ Máximo 100 caracteres
✅ Único no sistema

Password

✅ Obrigatório
✅ Entre 6 e 100 caracteres
✅ Pelo menos uma letra maiúscula
✅ Pelo menos uma letra minúscula
✅ Pelo menos um número


Última atualização: Janeiro 2025