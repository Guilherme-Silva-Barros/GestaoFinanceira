package com.asaasApi.AsaasApi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Cliente(String id, String name, String email, String mobilePhone, String cpfCnpj, String personType,
                      String cityName) {
}
