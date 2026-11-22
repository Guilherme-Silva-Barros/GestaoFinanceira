package com.asaasApi.AsaasApi.client;

import com.asaasApi.AsaasApi.dto.Cliente;
import com.asaasApi.AsaasApi.dto.Pagamento;
import com.asaasApi.AsaasApi.dto.Pages;
import com.asaasApi.AsaasApi.enuns.Status;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Client {

    private final RestClient httpClient;

    public Client(RestClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<Pagamento> listarPagamentos(Status status, LocalDate de, LocalDate ate) {
        List<Pagamento> pagamentos = new ArrayList<>();
        int offset = 0;
        Pages<Pagamento> page;

        do {
            int off = offset;
            page = httpClient.get().uri(u -> {
                var b = u.path("/payments").queryParam("offset", off).queryParam("limit", 100);
                if (status != null) {
                    b.queryParam("status", status);
                }
                if (de != null) {
                    b.queryParam("de", de);
                }
                if (ate != null) {
                    b.queryParam("ate", ate);
                }
                return b.build();
            }).retrieve().body(new ParameterizedTypeReference<Pages<Pagamento>>() {
            });
            pagamentos.addAll(page.data());
            offset += 100;
        } while (page.hasMore());
        return pagamentos;
    }

    public List<Cliente> listarCliente(String nome, String email) {
        List<Cliente> clientes = new ArrayList<>();
        int offset = 0;
        Pages<Cliente> page;
        do {
            int off = offset;
            page = httpClient.get().uri(u -> {
                var b = u.path("/customers").queryParam("offset", off).queryParam("limit", 100);
                return b.build();
            }).retrieve().body(new ParameterizedTypeReference<Pages<Cliente>>() {
            });
            clientes.addAll(page.data());
            offset += 100;
        } while (page.hasMore());
        return clientes;
    }


}
