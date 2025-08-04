package com.authservice.model.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RegisterResponse(
        @JsonProperty("message") String message) {
}
