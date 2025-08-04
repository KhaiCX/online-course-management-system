package com.authservice.model.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RefreshTokenRequest(
        @JsonProperty("refresh_token") String refreshToken) {
}
