package com.mrxunim.investimentPlataform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados para atualização de usuário (todos os campos são opcionais)")
public class UpdatedUserDTO {

    @Schema(
            description = "Novo nome do usuário",
            example = "João da Silva",
            minLength = 3,
            maxLength = 25
    )
    @Size(min = 3, max = 25, message = "O nome de usuário deve ter entre 3 e 25 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "O nome de usuário deve conter apenas letras e espaços")
    private String username;

    @Schema(
            description = "Nova senha do usuário",
            example = "NovaSenha123",
            minLength = 6,
            maxLength = 100,
            format = "password"
    )
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número"
    )
    private String password;
}