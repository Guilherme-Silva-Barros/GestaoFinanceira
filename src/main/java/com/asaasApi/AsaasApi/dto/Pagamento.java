package com.asaasApi.AsaasApi.dto;

import com.asaasApi.AsaasApi.enuns.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Pagamento(String id, String customer, BigDecimal value, Status status, LocalDate dueDate,
                        LocalDate paymentDate, String billingType) {
}
