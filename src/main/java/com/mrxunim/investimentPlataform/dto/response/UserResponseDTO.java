package com.mrxunim.investimentPlataform.dto.response;

import com.mrxunim.investimentPlataform.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados do usuário (sem senha)")
public class UserResponseDTO {

    @Schema(description = "ID único do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID userId;

    @Schema(description = "Nome do usuário", example = "João Silva")
    private String username;

    @Schema(description = "Email do usuário", example = "joao@example.com")
    private String email;

    @Schema(description = "Role/permissão do usuário", example = "USER")
    private Role role;

    @Schema(description = "Data de criação do usuário", example = "2024-01-20T10:30:00Z")
    private Instant createdAt;

    @Schema(description = "Data da última atualização", example = "2024-01-20T10:30:00Z")
    private Instant updatedAt;
}