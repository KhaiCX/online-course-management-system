package com.authservice.model.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record LoginRequest(
        @JsonProperty("username") String username,
        @JsonProperty("password") String password) {
}
