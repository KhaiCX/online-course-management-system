package com.authservice.model.http.request;

import lombok.Builder;

@Builder
public record RegisterRequest(String username, String password) {
}
