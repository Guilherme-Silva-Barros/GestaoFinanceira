package com.asaasApi.AsaasApi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;


@Configuration
@EnableConfigurationProperties(AsaasConfig.class)
public class AsaasConfig {

    @Bean
    RestClient restClient(AsaasPropeties props) {
        return RestClient.builder().baseUrl(props.baseUrl()).defaultHeader("acess_token", props.apiKey()).defaultHeader(HttpHeaders.USER_AGENT, props.userAgent()).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }
}
