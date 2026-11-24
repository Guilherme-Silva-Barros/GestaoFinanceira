package com.asaasApi.AsaasApi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(AsaasProperties.class)
public class AsaasConfig {

    @Bean
    RestClient restClient(AsaasProperties props) {
        HttpClient jdk = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(jdk);
        factory.setReadTimeout(Duration.ofSeconds(15));
        if (props.apiKey().contains("${")) {
            throw new IllegalStateException("Variável ASAAS_API_KEY não definida");
        }
        return RestClient.builder()

                .requestFactory(factory)
                .baseUrl(props.baseUrl())
                .defaultHeader("access_token",
                        props.apiKey())
                .defaultHeader(
                        HttpHeaders.USER_AGENT,
                        props.userAgent())
                .build();

    }
}
