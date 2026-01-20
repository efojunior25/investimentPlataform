package com.mrxunim.investimentPlataform.service;

import com.mrxunim.investimentPlataform.dto.LoginRequestDTO;
import com.mrxunim.investimentPlataform.dto.LoginResponseDTO;
import com.mrxunim.investimentPlataform.exception.InvalidCredentialsException;
import com.mrxunim.investimentPlataform.repository.UserRepository;
import com.mrxunim.investimentPlataform.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Tentativa de login para o email: {}", request.getEmail());

        try {
            // Autentica o usuário
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Credenciais inválidas para o email: {}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        // Busca o usuário no banco
        UserDetails user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado após autenticação: {}", request.getEmail());
                    return new InvalidCredentialsException();
                });

        // Gera o token
        String jwtToken = jwtService.generateToken(user);

        log.info("Login realizado com sucesso para: {}", request.getEmail());

        return LoginResponseDTO.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}