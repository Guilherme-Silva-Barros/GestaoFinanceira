package com.asaasApi.AsaasApi.service;

import com.asaasApi.AsaasApi.client.Client;
import com.asaasApi.AsaasApi.dto.Cliente;
import com.asaasApi.AsaasApi.dto.ClienteCobranca;
import com.asaasApi.AsaasApi.dto.Pagamento;
import com.asaasApi.AsaasApi.enuns.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AsaasService {

    private final Client asaasClient;

    public AsaasService(Client Asaasclient) {
        this.asaasClient = Asaasclient;
    }
    //metodo para mapear os clientes <->cobrancas
    public List<ClienteCobranca> relatorioCobrancas(Status status, LocalDate de, LocalDate ate, String nome, String email ) {
       //consultar a lista de clientes, transformar essa lista em uma sstream para melhor mapeamento e filtragem e
       //tratando duplicidades de Id com Lambdas
        Map<String, Cliente> clientById= asaasClient.listarCliente(nome, email)
                .stream()
                .collect(Collectors.toMap(Cliente::id, Function.identity(), (a,b) -> a));

        //mapeando cada cliente aos seus respectivos pagamentos
        return asaasClient.listarPagamentos(status, de, ate)
                .stream()
                .filter(pagamentos->clientById.containsKey(pagamentos.customer()))
                .map(pagamentos->{
                    Cliente client =  clientById.get(pagamentos.customer());
                    return new ClienteCobranca(
                            pagamentos.id(),
                            pagamentos.customer(),
                            client.name(),
                            client.email(),
                            client.cpfCnpj(),
                            pagamentos.value(),
                            pagamentos.status(),
                            pagamentos.dueDate(),
                            pagamentos.paymentDate(),
                            pagamentos.billingType());
                })
                .toList();
    }

   //metodo para filtrar os melhores pagadores


}
