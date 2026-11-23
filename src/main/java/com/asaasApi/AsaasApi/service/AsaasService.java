package com.asaasApi.AsaasApi.service;

import com.asaasApi.AsaasApi.client.AsaasClient;
import com.asaasApi.AsaasApi.dto.Cliente;
import com.asaasApi.AsaasApi.dto.ClienteCobranca;
import com.asaasApi.AsaasApi.dto.MelhoresPagadoresDTO;
import com.asaasApi.AsaasApi.dto.Pagamento;
import com.asaasApi.AsaasApi.enums.CampDate;
import com.asaasApi.AsaasApi.enums.Status;
import com.asaasApi.AsaasApi.exceptions.ParametroInvalidoException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AsaasService {

    private final AsaasClient asaasClient;
    private static final Set<Status> PAGOS = EnumSet.of(Status.RECEIVED, Status.CONFIRMED, Status.RECEIVED_IN_CASH);
    public AsaasService(AsaasClient Asaasclient) {
        this.asaasClient = Asaasclient;
    }
    //metodo para mapear os clientes <->cobrancas
    public List<ClienteCobranca> relatorioCobrancas(Status status, CampDate campDate, LocalDate de, LocalDate ate, String nome, String email ) {
       //consultar a lista de clientes, transformar essa lista em uma sstream para melhor mapeamento e filtragem e
       //tratando duplicidades de Id com Lambdas
        Map<String, Cliente> clientById= asaasClient.listarCliente(nome, email)
                .stream()
                .collect(Collectors.toMap(Cliente::id, Function.identity(), (a,b) -> a));

        //mapeando cada cliente aos seus respectivos pagamentos
        return asaasClient.listarPagamentos(status,campDate, de, ate)
                .stream()
                .filter(pagamentos->clientById.containsKey(pagamentos.customer()))
                .filter(p -> de == null || (dataDe(p, campDate) != null && !dataDe(p, campDate).isBefore(de)))
                .filter(p -> ate == null || (dataDe(p, campDate) != null && !dataDe(p, campDate).isAfter(ate)))
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
    private static LocalDate dataDe(Pagamento p, CampDate campo) {
        return campo == CampDate.PAGAMENTO ? p.paymentDate() : p.dueDate();
    }
   //metodo para filtrar os melhores pagadores
    public List<MelhoresPagadoresDTO>melhoresPagadores(LocalDate de, LocalDate ate){
        if (de != null && ate != null && de.isAfter(ate)) {
            throw new ParametroInvalidoException("O parâmetro 'de' não pode ser posterior a 'ate'.");
        }
        return relatorioCobrancas(null,CampDate.PAGAMENTO, de, ate, null, null)
                .stream()
                .filter(p->PAGOS.contains(p.status()))
                .collect(Collectors.groupingBy(ClienteCobranca::idCliente))
                .values().stream()
                .map(lista->{
                    ClienteCobranca primeiro = lista.get(0);
                    BigDecimal total = lista.stream()
                            .map(ClienteCobranca::valor)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new MelhoresPagadoresDTO(primeiro.idCliente(), primeiro.nome(), total);

                })
                .sorted(Comparator.comparing(MelhoresPagadoresDTO::total).reversed())
                .limit(15)
                .toList();
    }

}
