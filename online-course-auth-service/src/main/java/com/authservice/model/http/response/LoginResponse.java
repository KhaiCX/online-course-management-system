package com.authservice.model.http.response;

import lombok.Builder;

@Builder
public record LoginResponse(String token) {
}
