package com.mrxunim.investimentPlataform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Implementação do RFC 7807 - Problem Details for HTTP APIs
 * https://datatracker.ietf.org/doc/html/rfc7807
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {

    /**
     * URI de referência que identifica o tipo de problema
     * Exemplo: "https://api.investmentplatform.com/problems/validation-error"
     */
    private String type;

    /**
     * Título curto e legível do problema
     * Exemplo: "Validation Error"
     */
    private String title;

    /**
     * Código de status HTTP
     * Exemplo: 400
     */
    private Integer status;

    /**
     * Explicação detalhada e específica do problema
     * Exemplo: "O email fornecido já está em uso"
     */
    private String detail;

    /**
     * URI que identifica a instância específica do problema
     * Exemplo: "/v1/users"
     */
    private String instance;

    /**
     * Timestamp do erro
     */
    private LocalDateTime timestamp;

    /**
     * ID do trace para rastreamento (opcional)
     */
    private String traceId;

    /**
     * Propriedades adicionais específicas do problema
     * Exemplo: erros de validação, stack trace (dev), etc
     */
    private Map<String, Object> extensions;

    /**
     * Factory method para criar ProblemDetail básico
     */
    public static ProblemDetail forStatus(int status) {
        return ProblemDetail.builder()
                .status(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Factory method para criar ProblemDetail com tipo e título
     */
    public static ProblemDetail forStatusAndDetail(int status, String title, String detail) {
        return ProblemDetail.builder()
                .status(status)
                .title(title)
                .detail(detail)
                .timestamp(LocalDateTime.now())
                .build();
    }
}