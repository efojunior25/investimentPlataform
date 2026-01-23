package com.mrxunim.investimentPlataform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        // Definir o esquema de segurança JWT
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // Informações da API
                .info(new Info()
                        .title("Investment Platform API")
                        .description("""
                                API RESTful para gerenciamento de investimentos.
                                
                                ## Recursos Principais
                                - 🔐 Autenticação JWT
                                - 👥 Gerenciamento de usuários
                                - 💰 Carteira de investimentos (em breve)
                                - 📊 Dashboard de métricas (em breve)
                                
                                ## Autenticação
                                1. Cadastre-se em `/v1/users` (POST)
                                2. Faça login em `/v1/auth/login` (POST)
                                3. Use o token JWT retornado no header `Authorization: Bearer {token}`
                                
                                ## Tratamento de Erros
                                A API segue o padrão RFC 7807 (Problem Details).
                                Todos os erros retornam um objeto JSON estruturado com:
                                - `type`: URI do tipo de problema
                                - `title`: Título do erro
                                - `status`: Código HTTP
                                - `detail`: Descrição detalhada
                                - `traceId`: ID para rastreamento
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seu.email@example.com")
                                .url("https://github.com/mrxunim"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))

                // Servidores
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.investmentplatform.com")
                                .description("Servidor de Produção (exemplo)")
                ))

                // Configuração de segurança JWT
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Insira o token JWT obtido no endpoint de login")
                        )
                );
    }
}