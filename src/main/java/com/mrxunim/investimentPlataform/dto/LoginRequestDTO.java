package com.mrxunim.investimentPlataform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Credenciais para login")
public class LoginRequestDTO {

    @Schema(
            description = "Email do usuário",
            example = "joao@example.com",
            required = true
    )
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;

    @Schema(
            description = "Senha do usuário",
            example = "Senha123",
            required = true,
            format = "password"
    )
    @NotBlank(message = "A senha é obrigatória")
    private String password;
}