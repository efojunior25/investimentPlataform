package com.mrxunim.investimentPlataform.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedUserDTO {

    @Size(min = 3, max = 25, message = "O nome de usuário deve ter entre 3 e 25 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "O nome de usuário deve conter apenas letras e espaços")
    private String username;

    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula e um número"
    )
    private String password;
}