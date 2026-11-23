package com.asaasApi.AsaasApi.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "asaas.api")
public record AsaasProperties(
        @NotBlank  String baseUrl,
        @NotBlank  String apiKey,
        @NotBlank  String userAgent) {
}
