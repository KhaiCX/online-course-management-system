package com.authservice.exception;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String token) {
        super(String.format("Refresh token {} is not valid", token));
    }

}
