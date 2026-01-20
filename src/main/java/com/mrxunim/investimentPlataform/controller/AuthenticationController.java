package com.mrxunim.investimentPlataform.controller;

import com.mrxunim.investimentPlataform.dto.LoginRequestDTO;
import com.mrxunim.investimentPlataform.dto.LoginResponseDTO;
import com.mrxunim.investimentPlataform.dto.response.ApiResponse;
import com.mrxunim.investimentPlataform.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {

        LoginResponseDTO response = authenticationService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success("Login realizado com sucesso", response)
        );
    }
}