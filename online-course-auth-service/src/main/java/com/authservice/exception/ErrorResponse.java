package com.authservice.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(
        String timestamp,
        Integer status,
        String error,
        String message,
        String path,
        String errorCode) {
}
