package com.mrxunim.investimentPlataform.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro para adicionar informações de contexto ao MDC (Mapped Diagnostic Context)
 * e fazer logging estruturado de todas as requisições
 */
@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {
            // Gera um trace ID único para a requisição
            String traceId = UUID.randomUUID().toString();
            MDC.put("trace_id", traceId);

            // Adiciona request ID
            String requestId = request.getHeader("X-Request-ID");
            if (requestId == null) {
                requestId = UUID.randomUUID().toString();
            }
            MDC.put("request_id", requestId);

            // Adiciona informações do usuário autenticado (se houver)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
                MDC.put("user_id", auth.getName());
            }

            // Adiciona trace ID no response header
            response.setHeader("X-Trace-ID", traceId);
            response.setHeader("X-Request-ID", requestId);

            // Log da requisição
            log.info("Incoming request: {} {} from {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    getClientIp(request));

            // Continua a cadeia de filtros
            filterChain.doFilter(request, response);

            // Log da resposta
            long duration = System.currentTimeMillis() - startTime;
            log.info("Completed request: {} {} - Status: {} - Duration: {}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);

        } finally {
            // Limpa o MDC após a requisição
            MDC.clear();
        }
    }

    /**
     * Obtém o IP real do cliente, considerando proxies
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Não aplica o filtro para recursos estáticos
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/static/") ||
                path.startsWith("/public/") ||
                path.startsWith("/actuator/health");
    }
}