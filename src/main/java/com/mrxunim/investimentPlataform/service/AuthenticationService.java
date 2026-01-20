package com.mrxunim.investimentPlataform.service;

import com.mrxunim.investimentPlataform.dto.LoginRequestDTO;
import com.mrxunim.investimentPlataform.dto.LoginResponseDTO;
import com.mrxunim.investimentPlataform.repository.UserRepository;
import com.mrxunim.investimentPlataform.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Realiza o login do usuário e retorna o token JWT
     */
    public LoginResponseDTO login(LoginRequestDTO request) {
        // Autentica o usuário
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Busca o usuário no banco
        UserDetails user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Gera o token
        String jwtToken = jwtService.generateToken(user);

        return LoginResponseDTO.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}