package com.asaasApi.AsaasApi.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "asaas.api")
public record AsaasProperties(
        @NotBlank  @Validated String baseUrl,
        @NotBlank  @Validated String apiKey,
        @NotBlank  @Validated String userAgent) {
}
