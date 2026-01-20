package com.mrxunim.investimentPlataform.controller;

import com.mrxunim.investimentPlataform.dto.CreateUserDTO;
import com.mrxunim.investimentPlataform.dto.UpdatedUserDTO;
import com.mrxunim.investimentPlataform.entity.User;
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
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        UUID userId = userService.createUser(createUserDTO);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    /**
     * Listar todos os usuários - apenas ADMIN
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    /**
     * Buscar usuário por ID - usuário autenticado pode ver seu próprio perfil ou ADMIN pode ver qualquer um
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(
            @PathVariable("userId") String userId,
            Authentication authentication) {

        UUID requestedUserId = UUID.fromString(userId);
        User currentUser = (User) authentication.getPrincipal();

        // Verifica se o usuário está tentando acessar seu próprio perfil ou se é ADMIN
        if (!currentUser.getUserId().equals(requestedUserId) &&
                currentUser.getAuthorities().stream()
                        .noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build();
        }

        return userService.getUserById(requestedUserId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletar usuário - apenas ADMIN
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable("userId") String userId) {
        userService.deleteById(UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualizar usuário - apenas o próprio usuário pode se atualizar
     */
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdatedUserDTO updatedUserDTO,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        // Verifica se o usuário está tentando atualizar seu próprio perfil
        if (!currentUser.getUserId().equals(userId)) {
            return ResponseEntity.status(403).body("Você só pode atualizar seu próprio perfil");
        }

        try {
            String result = userService.updateUser(userId, updatedUserDTO);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}