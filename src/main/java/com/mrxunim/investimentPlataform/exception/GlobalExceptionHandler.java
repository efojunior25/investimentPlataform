package com.mrxunim.investimentPlataform.exception;

import com.mrxunim.investimentPlataform.dto.response.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.error.include-stacktrace:never}")
    private String includeStacktrace;

    private static final String PROBLEM_BASE_URL = "https://api.investmentplatform.com/problems";

    /**
     * Trata erros de validação de Bean Validation (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        // Extrai erros de validação
        Map<String, List<String>> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            validationErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        Map<String, Object> extensions = new HashMap<>();
        extensions.put("validationErrors", validationErrors);
        extensions.put("invalidFieldsCount", validationErrors.size());

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/validation-error")
                .title("Validation Error")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("Um ou mais campos contêm valores inválidos")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .extensions(extensions)
                .build();

        log.warn("[{}] Validation error on {}: {} field(s) invalid",
                traceId, request.getRequestURI(), validationErrors.size());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    /**
     * Trata recurso não encontrado (404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/resource-not-found")
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();

        log.warn("[{}] Resource not found: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    /**
     * Trata recurso duplicado (409)
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateResourceException(
            DuplicateResourceException ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/duplicate-resource")
                .title("Duplicate Resource")
                .status(HttpStatus.CONFLICT.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();

        log.warn("[{}] Duplicate resource: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    /**
     * Trata credenciais inválidas (401)
     */
    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    public ResponseEntity<ProblemDetail> handleInvalidCredentialsException(
            Exception ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();
        String clientIp = getClientIp(request);

        Map<String, Object> extensions = new HashMap<>();
        extensions.put("clientIp", clientIp);

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/invalid-credentials")
                .title("Invalid Credentials")
                .status(HttpStatus.UNAUTHORIZED.value())
                .detail("Email ou senha inválidos")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .extensions(extensions)
                .build();

        log.warn("[{}] Invalid credentials attempt from IP: {}", traceId, clientIp);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    /**
     * Trata não autorizado (401)
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/unauthorized")
                .title("Unauthorized")
                .status(HttpStatus.UNAUTHORIZED.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();

        log.warn("[{}] Unauthorized access: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    /**
     * Trata acesso negado (403)
     */
    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class})
    public ResponseEntity<ProblemDetail> handleForbiddenException(
            Exception ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();
        String userId = MDC.get("user_id");

        Map<String, Object> extensions = new HashMap<>();
        if (userId != null) {
            extensions.put("userId", userId);
        }

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/forbidden")
                .title("Forbidden")
                .status(HttpStatus.FORBIDDEN.value())
                .detail("Você não tem permissão para acessar este recurso")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .extensions(extensions.isEmpty() ? null : extensions)
                .build();

        log.warn("[{}] Forbidden access attempt by user: {}", traceId, userId);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    /**
     * Trata IllegalArgumentException (400)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/invalid-argument")
                .title("Invalid Argument")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();

        log.warn("[{}] Invalid argument: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    /**
     * Trata erro de parsing JSON (400)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/malformed-request")
                .title("Malformed Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("O corpo da requisição está mal formatado ou contém dados inválidos")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();

        log.warn("[{}] Malformed request body: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    /**
     * Trata erro de tipo de argumento (400)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        Map<String, Object> extensions = new HashMap<>();
        extensions.put("parameter", ex.getName());
        extensions.put("expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        extensions.put("providedValue", ex.getValue());

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/type-mismatch")
                .title("Type Mismatch")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(String.format("O parâmetro '%s' deve ser do tipo %s",
                        ex.getName(),
                        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "válido"))
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .extensions(extensions)
                .build();

        log.warn("[{}] Type mismatch for parameter '{}': {}", traceId, ex.getName(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    /**
     * Trata violação de integridade de dados (409)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/data-integrity-violation")
                .title("Data Integrity Violation")
                .status(HttpStatus.CONFLICT.value())
                .detail("Violação de restrição de integridade de dados. Verifique se não há duplicação de valores únicos.")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .build();

        log.error("[{}] Data integrity violation: {}", traceId, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    /**
     * Trata todas as outras exceções não mapeadas (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        String traceId = getOrCreateTraceId();

        Map<String, Object> extensions = new HashMap<>();
        extensions.put("exceptionType", ex.getClass().getSimpleName());

        // Inclui stack trace apenas em desenvolvimento
        if ("always".equals(includeStacktrace)) {
            extensions.put("stackTrace", Arrays.stream(ex.getStackTrace())
                    .limit(10)
                    .map(StackTraceElement::toString)
                    .collect(Collectors.toList()));
        }

        ProblemDetail problem = ProblemDetail.builder()
                .type(PROBLEM_BASE_URL + "/internal-error")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde.")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .traceId(traceId)
                .extensions(extensions)
                .build();

        log.error("[{}] Unexpected error on {}: {}", traceId, request.getRequestURI(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    /**
     * Obtém ou cria um trace ID para rastreamento
     */
    private String getOrCreateTraceId() {
        String traceId = MDC.get("trace_id");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            MDC.put("trace_id", traceId);
        }
        return traceId;
    }

    /**
     * Obtém o IP do cliente
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
}