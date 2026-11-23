package com.asaasApi.AsaasApi.enums;

public enum CampDate {
    VENCIMENTO("dueDate"),
    PAGAMENTO("paymentDate");

    private final String campoApi;
    CampDate(String campoApi) {
        this.campoApi = campoApi;
    }
    public String campoApi() {
        return campoApi;
    }
}
