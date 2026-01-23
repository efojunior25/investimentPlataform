package com.mrxunim.investimentPlataform.controller;

import com.mrxunim.investimentPlataform.dto.LoginRequestDTO;
import com.mrxunim.investimentPlataform.dto.LoginResponseDTO;
import com.mrxunim.investimentPlataform.dto.response.ApiResponse;
import com.mrxunim.investimentPlataform.dto.response.ProblemDetail;
import com.mrxunim.investimentPlataform.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
            summary = "Realizar login",
            description = """
                    Autentica um usuário e retorna um token JWT.
                    
                    O token deve ser usado no header `Authorization: Bearer {token}` para acessar endpoints protegidos.
                    
                    **Tempo de expiração:** 24 horas (86400000 ms)
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "message": "Login realizado com sucesso",
                                              "data": {
                                                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                                "type": "Bearer",
                                                "expiresIn": 86400000
                                              },
                                              "timestamp": "2024-01-20T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Dados de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "https://api.investmentplatform.com/problems/validation-error",
                                              "title": "Validation Error",
                                              "status": 400,
                                              "detail": "Um ou mais campos contêm valores inválidos",
                                              "instance": "/v1/auth/login",
                                              "timestamp": "2024-01-20T10:30:00",
                                              "traceId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                                              "extensions": {
                                                "validationErrors": {
                                                  "email": ["O email deve ser válido"]
                                                }
                                              }
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "https://api.investmentplatform.com/problems/invalid-credentials",
                                              "title": "Invalid Credentials",
                                              "status": 401,
                                              "detail": "Email ou senha inválidos",
                                              "instance": "/v1/auth/login",
                                              "timestamp": "2024-01-20T10:30:00",
                                              "traceId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de login",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequestDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "email": "joao@example.com",
                                              "password": "Senha123"
                                            }
                                            """
                            )
                    )
            )
            LoginRequestDTO request) {

        LoginResponseDTO response = authenticationService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success("Login realizado com sucesso", response)
        );
    }
}