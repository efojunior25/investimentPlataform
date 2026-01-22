package com.mrxunim.investimentPlataform.controller;

import com.mrxunim.investimentPlataform.dto.CreateUserDTO;
import com.mrxunim.investimentPlataform.dto.UpdatedUserDTO;
import com.mrxunim.investimentPlataform.dto.response.ApiResponse;
import com.mrxunim.investimentPlataform.dto.response.UserResponseDTO;
import com.mrxunim.investimentPlataform.entity.User;
import com.mrxunim.investimentPlataform.exception.ForbiddenException;
import com.mrxunim.investimentPlataform.service.UserService;
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
public class UserController {

    private final UserService userService;

    /**
     * Cadastro de novo usuário - endpoint público
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody CreateUserDTO createUserDTO) {

        UserResponseDTO user = userService.createUser(createUserDTO);

        return ResponseEntity
                .created(URI.create("/v1/users/" + user.getUserId().toString()))
                .body(ApiResponse.success("Usuário criado com sucesso", user));
    }

    /**
     * Listar todos os usuários - apenas ADMIN
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> listUsers() {
        List<UserResponseDTO> users = userService.listUsers();
        return ResponseEntity.ok(
                ApiResponse.success("Usuários listados com sucesso", users)
        );
    }

    /**
     * Buscar usuário por ID
     * Usuário pode ver seu próprio perfil ou ADMIN pode ver qualquer um
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(
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

    /**
     * Atualizar usuário - apenas o próprio usuário
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdatedUserDTO updatedUserDTO,
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

    /**
     * Deletar usuário - apenas ADMIN
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable("userId") UUID userId) {
        userService.deleteById(userId);
        return ResponseEntity.ok(
                ApiResponse.success("Usuário deletado com sucesso", null)
        );
    }
}