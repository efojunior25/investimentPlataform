package com.mrxunim.investimentPlataform.service;

import com.mrxunim.investimentPlataform.dto.CreateUserDTO;
import com.mrxunim.investimentPlataform.dto.UpdatedUserDTO;
import com.mrxunim.investimentPlataform.dto.response.UserResponseDTO;
import com.mrxunim.investimentPlataform.entity.Role;
import com.mrxunim.investimentPlataform.entity.User;
import com.mrxunim.investimentPlataform.exception.DuplicateResourceException;
import com.mrxunim.investimentPlataform.exception.ResourceNotFoundException;
import com.mrxunim.investimentPlataform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
        log.info("Criando novo usuário com email: {}", createUserDTO.getEmail());

        // Verifica se email já existe
        if (userRepository.findByEmail(createUserDTO.getEmail()).isPresent()) {
            log.warn("Tentativa de cadastro com email duplicado: {}", createUserDTO.getEmail());
            throw new DuplicateResourceException("Usuário", "email", createUserDTO.getEmail());
        }

        User user = User.builder()
                .username(createUserDTO.getUsername())
                .email(createUserDTO.getEmail())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Usuário criado com sucesso: {}", savedUser.getUserId());

        return mapToResponseDTO(savedUser);
    }

    public List<UserResponseDTO> listUsers() {
        log.info("Listando todos os usuários");
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(UUID userId) {
        log.info("Buscando usuário por ID: {}", userId);
        return userRepository.findById(userId)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", userId);
                    return new ResourceNotFoundException("Usuário", "userId", userId);
                });
    }

    @Transactional
    public UserResponseDTO updateUser(UUID userId, UpdatedUserDTO updatedUserDTO) {
        log.info("Atualizando usuário: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Tentativa de atualizar usuário inexistente: {}", userId);
                    return new ResourceNotFoundException("Usuário", "userId", userId);
                });

        boolean updated = false;

        if (updatedUserDTO.getUsername() != null && !updatedUserDTO.getUsername().isBlank()) {
            user.setUsername(updatedUserDTO.getUsername());
            updated = true;
        }

        if (updatedUserDTO.getPassword() != null && !updatedUserDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUserDTO.getPassword()));
            updated = true;
        }

        if (updated) {
            User savedUser = userRepository.save(user);
            log.info("Usuário atualizado com sucesso: {}", userId);
            return mapToResponseDTO(savedUser);
        }

        log.info("Nenhuma alteração necessária para o usuário: {}", userId);
        return mapToResponseDTO(user);
    }

    @Transactional
    public void deleteById(UUID userId) {
        log.info("Deletando usuário: {}", userId);

        if (!userRepository.existsById(userId)) {
            log.warn("Tentativa de deletar usuário inexistente: {}", userId);
            throw new ResourceNotFoundException("Usuário", "userId", userId);
        }

        userRepository.deleteById(userId);
        log.info("Usuário deletado com sucesso: {}", userId);
    }

    /**
     * Mapeia User para UserResponseDTO (sem senha)
     */
    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}