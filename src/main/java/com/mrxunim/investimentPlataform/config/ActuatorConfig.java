package com.mrxunim.investimentPlataform.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuração para endpoints de monitoramento e health checks
 *
 * NOTA: HTTP Trace foi removido no Spring Boot 3.x
 * Use Observability com Micrometer Tracing se precisar de trace completo
 */
@Configuration
public class ActuatorConfig {

    // Use as configurações no application.properties para habilitar endpoints

}