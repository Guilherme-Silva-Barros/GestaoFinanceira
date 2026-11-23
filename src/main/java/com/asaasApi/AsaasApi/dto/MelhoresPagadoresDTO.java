package com.asaasApi.AsaasApi.dto;

import java.math.BigDecimal;

public record MelhoresPagadoresDTO(
        String idCliente,
        String nome,
        BigDecimal total
) {
}
