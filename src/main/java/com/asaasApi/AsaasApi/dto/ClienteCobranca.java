package com.asaasApi.AsaasApi.dto;

import com.asaasApi.AsaasApi.enuns.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClienteCobranca(String idCobranca, String idCliente, String nome, String email, String cpfCnpj,
                              BigDecimal valor, Status status, LocalDate vencimento, LocalDate dataPagmento,
                              String tipoCobranca) {
}
