package com.mrxunim.investimentPlataform.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedUserDTO {

    @Size(min = 3, max = 25, message = "Username deve ter entre 3 e 25 caracteres")
    private String username;

    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;
}