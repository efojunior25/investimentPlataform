package com.mrxunim.investimentPlataform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para criação de um novo usuário")
public class CreateUserDTO {

    @Schema(
            description = "Nome completo do usuário",
            example = "João Silva",
            required = true,
            minLength = 3,
            maxLength = 25
    )
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 25, message = "O nome de usuário deve ter entre 3 e 25 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "O nome de usuário deve conter apenas letras e espaços")
    private String username;

    @Schema(
            description = "Endereço de email do usuário (único no sistema)",
            example = "joao@example.com",
            required = true,
            maxLength = 100
    )
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
    private String email;

    @Schema(
            description = "Senha do usuário (deve conter maiúscula, minúscula e número)",
            example = "Senha123",
            required = true,
            minLength = 6,
            maxLength = 100,
            format = "password"
    )
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número"
    )
    private String password;
}