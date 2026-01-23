package com.mrxunim.investimentPlataform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta do login com token JWT")
public class LoginResponseDTO {

    @Schema(
            description = "Token JWT para autenticação",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String token;

    @Schema(
            description = "Tipo do token",
            example = "Bearer",
            defaultValue = "Bearer"
    )
    @Builder.Default
    private String type = "Bearer";

    @Schema(
            description = "Tempo de expiração do token em milissegundos",
            example = "86400000"
    )
    private Long expiresIn;
}