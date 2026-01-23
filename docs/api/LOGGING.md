📊 Sistema de Logs - Investment Platform
📋 Índice

Visão Geral
Configuração de Logs
Níveis de Log
Logs Estruturados
Trace ID e Rastreamento
Logs por Ambiente
Análise de Logs


🎯 Visão Geral
O sistema utiliza Logback com Logstash Encoder para gerar logs estruturados em formato JSON, facilitando análise e monitoramento.
Recursos Principais

✅ Logs estruturados em JSON
✅ Trace ID para rastreamento de requisições
✅ Contexto de usuário autenticado
✅ Separação por níveis (INFO, WARN, ERROR)
✅ Rotação automática de arquivos
✅ Logs de auditoria
✅ Performance tracking


⚙️ Configuração de Logs
Arquivos de Log
logs/
├── investment-platform.log          # Logs em texto (desenvolvimento)
├── investment-platform-json.log     # Logs estruturados (produção)
├── investment-platform-error.log    # Apenas erros
└── investment-platform-2024-01-20.log  # Arquivos rotacionados
Rotação de Arquivos

Período: Diário
Retenção: 30 dias (logs normais), 90 dias (erros)
Tamanho máximo: 1GB total


📊 Níveis de Log
TRACE
Informações muito detalhadas para debugging profundo.
javalog.trace("Parâmetros da query: {}", params);
DEBUG
Informações úteis para desenvolvimento e debugging.
javalog.debug("Processando requisição para usuário: {}", userId);
INFO
Eventos importantes do sistema.
javalog.info("Usuário {} criado com sucesso", email);
WARN
Situações potencialmente problemáticas.
javalog.warn("Tentativa de login com email inválido: {}", email);
ERROR
Erros que precisam atenção.
javalog.error("Falha ao conectar com o banco de dados", exception);

🔍 Logs Estruturados
Formato JSON
json{
"@timestamp": "2024-01-20T10:30:45.123Z",
"level": "INFO",
"thread": "http-nio-8080-exec-1",
"logger": "com.mrxunim.investimentPlataform.service.UserService",
"message": "Usuário criado com sucesso",
"trace_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
"request_id": "req-123456",
"user_id": "joao@example.com",
"context": {
"userId": "550e8400-e29b-41d4-a716-446655440000",
"email": "joao@example.com"
}
}
Campos Padrão
CampoDescrição@timestampData/hora do loglevelNível do log (INFO, WARN, ERROR, etc)threadThread que gerou o logloggerClasse que gerou o logmessageMensagem do logtrace_idID único da requisiçãorequest_idID da requisição HTTPuser_idEmail do usuário autenticado

🔗 Trace ID e Rastreamento
O que é Trace ID?
Um identificador único gerado para cada requisição HTTP, permitindo rastrear todas as operações relacionadas.
Como Funciona

Requisição recebida → Gera trace_id e request_id
Durante o processamento → Todos os logs incluem esses IDs
Resposta enviada → IDs incluídos nos headers

Headers de Resposta
httpX-Trace-ID: a1b2c3d4-e5f6-7890-abcd-ef1234567890
X-Request-ID: req-123456
Exemplo de Rastreamento
Requisição:
bashcurl -X POST http://localhost:8080/v1/users \
-H "Content-Type: application/json" \
-d '{"username":"João","email":"joao@example.com","password":"Senha123"}'
Logs gerados:
[a1b2c3d4] Incoming request: POST /v1/users from 127.0.0.1
[a1b2c3d4] Criando novo usuário com email: joao@example.com
[a1b2c3d4] Usuário criado com sucesso: 550e8400-e29b-41d4-a716-446655440000
[a1b2c3d4] Completed request: POST /v1/users - Status: 201 - Duration: 245ms

🌍 Logs por Ambiente
Desenvolvimento (dev)
properties# application-dev.properties
logging.level.root=DEBUG
logging.level.com.mrxunim.investimentPlataform=TRACE
server.error.include-stacktrace=always
Características:

Logs detalhados no console
SQL queries visíveis
Stack traces completos
Formato de texto legível

Produção (prod)
properties# application-prod.properties
logging.level.root=WARN
logging.level.com.mrxunim.investimentPlataform=INFO
server.error.include-stacktrace=never
Características:

Logs em JSON
Apenas INFO, WARN, ERROR
Sem stack traces públicos
Logs assíncronos para performance

Testes (test)
properties# application-test.properties
logging.level.root=WARN
logging.level.com.mrxunim.investimentPlataform=INFO
Características:

Logs mínimos
Foco em assertivas
Sem ruído nos testes


🔎 Análise de Logs
Buscar por Trace ID
bash# Buscar todos os logs de uma requisição específica
grep "a1b2c3d4-e5f6-7890-abcd-ef1234567890" logs/investment-platform-json.log
Buscar erros de um usuário
bash# Buscar erros de um usuário específico
grep "joao@example.com" logs/investment-platform-error.log
Análise de Performance
bash# Buscar requisições lentas (>1000ms)
grep "Duration: [1-9][0-9][0-9][0-9]ms" logs/investment-platform.log
Top Erros
bash# Ver os erros mais frequentes
grep "ERROR" logs/investment-platform.log | cut -d'-' -f5 | sort | uniq -c | sort -rn | head -10

📈 Exemplos de Logs
1. Criação de Usuário (Sucesso)
   json{
   "@timestamp": "2024-01-20T10:30:45.123Z",
   "level": "INFO",
   "logger": "c.m.i.service.UserService",
   "message": "Usuário criado com sucesso",
   "trace_id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
   "userId": "550e8400-e29b-41d4-a716-446655440000"
   }
2. Login Inválido (Warning)
   json{
   "@timestamp": "2024-01-20T10:35:12.456Z",
   "level": "WARN",
   "logger": "c.m.i.exception.GlobalExceptionHandler",
   "message": "Invalid credentials attempt from IP: 192.168.1.100",
   "trace_id": "b2c3d4e5-f6a7-8901-bcde-f23456789012",
   "clientIp": "192.168.1.100"
   }
3. Erro de Validação
   json{
   "@timestamp": "2024-01-20T10:40:33.789Z",
   "level": "WARN",
   "logger": "c.m.i.exception.GlobalExceptionHandler",
   "message": "Validation error on /v1/users: 3 field(s) invalid",
   "trace_id": "c3d4e5f6-a7b8-9012-cdef-345678901234",
   "validationErrors": {
   "email": ["O email deve ser válido"],
   "password": ["A senha deve ter entre 6 e 100 caracteres"]
   }
   }
4. Erro Interno (Error)
   json{
   "@timestamp": "2024-01-20T10:45:22.111Z",
   "level": "ERROR",
   "logger": "c.m.i.exception.GlobalExceptionHandler",
   "message": "Unexpected error on /v1/users: Connection refused",
   "trace_id": "d4e5f6a7-b8c9-0123-def4-56789012345",
   "exceptionType": "ConnectException",
   "stack_trace": [
   "java.net.ConnectException: Connection refused",
   "at com.mysql.cj.jdbc.ConnectionImpl.connect(...)"
   ]
   }
5. Performance Tracking
   2024-01-20 10:30:45.123 [http-nio-8080-exec-1] [a1b2c3d4] INFO  c.m.i.config.RequestLoggingFilter - Incoming request: POST /v1/users from 127.0.0.1
   2024-01-20 10:30:45.368 [http-nio-8080-exec-1] [a1b2c3d4] INFO  c.m.i.config.RequestLoggingFilter - Completed request: POST /v1/users - Status: 201 - Duration: 245ms

🛠️ Comandos Úteis
Monitorar logs em tempo real
bash# Todos os logs
tail -f logs/investment-platform.log

# Apenas erros
tail -f logs/investment-platform-error.log

# Logs estruturados
tail -f logs/investment-platform-json.log | jq '.'
Contar logs por nível
bashgrep -o '"level":"[A-Z]*"' logs/investment-platform-json.log | sort | uniq -c
Extrair apenas mensagens
bashcat logs/investment-platform-json.log | jq -r '.message'
Buscar logs de um período
bash# Logs entre 10h e 11h
grep "2024-01-20 10:" logs/investment-platform.log

🚀 Boas Práticas
✅ Faça

Use níveis apropriados (INFO para eventos, WARN para alertas, ERROR para erros)
Inclua contexto relevante (userId, traceId, etc)
Evite logs em loops de alto volume
Use logging estruturado com campos consistentes
Sanitize dados sensíveis (senhas, tokens)

❌ Não Faça

Logar senhas ou dados sensíveis
Usar System.out.println()
Logar exceptions sem contexto
Fazer log excessivo em produção
Ignorar erros sem logar

Exemplo Bom
javalog.info("Processando pagamento para usuário: {} - Valor: {} - Método: {}",
userId, amount, paymentMethod);
Exemplo Ruim
javaSystem.out.println("Processando pagamento");
log.info(user.toString()); // Pode conter dados sensíveis

📞 Troubleshooting
Logs não aparecem

Verifique o nível de log em application.properties
Confirme que a pasta logs/ existe e tem permissão de escrita
Verifique se o logback-spring.xml está correto

Logs muito volumosos

Ajuste o nível de log para WARN em produção
Revise logs desnecessários no código
Use logging assíncrono

Performance degradada

Use appenders assíncronos
Reduza o nível de log
Desative SQL logging em produção


Última atualização: Janeiro 2025