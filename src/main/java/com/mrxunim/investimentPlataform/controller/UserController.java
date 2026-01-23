package com.mrxunim.investimentPlataform.controller;

import com.mrxunim.investimentPlataform.dto.CreateUserDTO;
import com.mrxunim.investimentPlataform.dto.UpdatedUserDTO;
import com.mrxunim.investimentPlataform.dto.response.ApiResponse;
import com.mrxunim.investimentPlataform.dto.response.ProblemDetail;
import com.mrxunim.investimentPlataform.dto.response.UserResponseDTO;
import com.mrxunim.investimentPlataform.entity.User;
import com.mrxunim.investimentPlataform.exception.ForbiddenException;
import com.mrxunim.investimentPlataform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(
            summary = "Cadastrar novo usuário",
            description = """
                    Cria um novo usuário no sistema.
                    
                    **Endpoint público** - não requer autenticação.
                    
                    O usuário é criado com a role `USER` por padrão.
                    A senha é armazenada com hash BCrypt.
                    """
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
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
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Dados de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Email já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "https://api.investmentplatform.com/problems/duplicate-resource",
                                              "title": "Duplicate Resource",
                                              "status": 409,
                                              "detail": "Usuário já existe com email: 'joao@example.com'",
                                              "instance": "/v1/users",
                                              "timestamp": "2024-01-20T10:30:00",
                                              "traceId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo usuário",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CreateUserDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "username": "João Silva",
                                              "email": "joao@example.com",
                                              "password": "Senha123"
                                            }
                                            """
                            )
                    )
            )
            CreateUserDTO createUserDTO) {

        UserResponseDTO user = userService.createUser(createUserDTO);

        return ResponseEntity
                .created(URI.create("/v1/users/" + user.getUserId().toString()))
                .body(ApiResponse.success("Usuário criado com sucesso", user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna uma lista com todos os usuários cadastrados. **Apenas ADMIN**.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "success": true,
                                              "message": "Usuários listados com sucesso",
                                              "data": [
                                                {
                                                  "userId": "550e8400-e29b-41d4-a716-446655440000",
                                                  "username": "João Silva",
                                                  "email": "joao@example.com",
                                                  "role": "USER",
                                                  "createdAt": "2024-01-20T10:30:00Z",
                                                  "updatedAt": "2024-01-20T10:30:00Z"
                                                }
                                              ],
                                              "timestamp": "2024-01-20T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão (apenas ADMIN)",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> listUsers() {
        List<UserResponseDTO> users = userService.listUsers();
        return ResponseEntity.ok(
                ApiResponse.success("Usuários listados com sucesso", users)
        );
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Buscar usuário por ID",
            description = """
                    Retorna os dados de um usuário específico.
                    
                    **Permissões:**
                    - Usuário pode ver seu próprio perfil
                    - ADMIN pode ver qualquer perfil
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão para visualizar este perfil",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
            @Parameter(description = "ID do usuário", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable("userId") UUID userId,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        // Verifica permissão
        if (!currentUser.getUserId().equals(userId) &&
                currentUser.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ForbiddenException("Você não tem permissão para visualizar este perfil");
        }

        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{userId}")
    @Operation(
            summary = "Atualizar usuário",
            description = """
                    Atualiza os dados de um usuário.
                    
                    **Permissão:** Apenas o próprio usuário pode se atualizar.
                    
                    Campos opcionais - envie apenas o que deseja atualizar.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Você só pode atualizar seu próprio perfil",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @Parameter(description = "ID do usuário", required = true)
            @PathVariable UUID userId,
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados a serem atualizados (campos opcionais)",
                    content = @Content(
                            schema = @Schema(implementation = UpdatedUserDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "username": "João da Silva",
                                              "password": "NovaSenha123"
                                            }
                                            """
                            )
                    )
            )
            UpdatedUserDTO updatedUserDTO,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        // Verifica se está tentando atualizar seu próprio perfil
        if (!currentUser.getUserId().equals(userId)) {
            throw new ForbiddenException("Você só pode atualizar seu próprio perfil");
        }

        UserResponseDTO user = userService.updateUser(userId, updatedUserDTO);
        return ResponseEntity.ok(
                ApiResponse.success("Usuário atualizado com sucesso", user)
        );
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Deletar usuário",
            description = "Remove um usuário do sistema. **Apenas ADMIN**.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Usuário deletado com sucesso"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Sem permissão (apenas ADMIN)",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteById(
            @Parameter(description = "ID do usuário a ser deletado", required = true)
            @PathVariable("userId") UUID userId) {

        userService.deleteById(userId);
        return ResponseEntity.ok(
                ApiResponse.success("Usuário deletado com sucesso", null)
        );
    }
}