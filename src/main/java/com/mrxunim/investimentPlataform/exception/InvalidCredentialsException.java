package com.mrxunim.investimentPlataform.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException() {
        super("Email ou senha inválidos");
    }
}