package com.authservice.model.http.request;

import lombok.Builder;

@Builder
public record LoginRequest(String username, String password) {
}
