package com.mrxunim.investimentPlataform.dto.response;

import com.mrxunim.investimentPlataform.entity.Role;
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
public class UserResponseDTO {
    private UUID userId;
    private String username;
    private String email;
    private Role role;
    private Instant createdAt;
    private Instant updatedAt;
}
