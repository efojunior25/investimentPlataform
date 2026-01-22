package com.mrxunim.investimentPlataform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;

    @Builder.Default
    private String type = "Bearer";

    private Long expiresIn;
}