package com.mrxunim.investimentPlataform.service;

import com.mrxunim.investimentPlataform.dto.CreateUserDTO;
import com.mrxunim.investimentPlataform.dto.UpdatedUserDTO;
import com.mrxunim.investimentPlataform.entity.User;
import com.mrxunim.investimentPlataform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UUID createUser(CreateUserDTO createUserDTO) {
        if (userRepository.findByEmail(createUserDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        User userData = User.builder()
                .username(createUserDTO.getUsername())
                .email(createUserDTO.getEmail())
                .password(createUserDTO.getPassword())
                .build();

        return userRepository.save(userData).getUserId();
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID userId) {

        return userRepository.findById(userId);
    }

    @Transactional
    public String updateUser(UUID userId, UpdatedUserDTO updatedUserDTO) {
        return userRepository.findById(userId)
                .map(user -> {
                    if (updatedUserDTO.getUsername() != null) {
                        user.setUsername(updatedUserDTO.getUsername());
                    }
                    if (updatedUserDTO.getPassword() != null) {
                        user.setPassword(updatedUserDTO.getPassword());
                    }
                    user.setUpdatedAt(Instant.now());

                    userRepository.save(user);
                    return "Usuário atualizado com sucesso";
                })
                .orElse("Usuário não encontrado");
    }

    public void deleteById(UUID userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        }
    }
}
