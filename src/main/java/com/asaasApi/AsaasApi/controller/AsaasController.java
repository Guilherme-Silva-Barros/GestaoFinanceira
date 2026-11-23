package com.asaasApi.AsaasApi.controller;

import com.asaasApi.AsaasApi.dto.ClienteCobranca;
import com.asaasApi.AsaasApi.dto.MelhoresPagadoresDTO;
import com.asaasApi.AsaasApi.enums.CampDate;
import com.asaasApi.AsaasApi.enums.Status;
import com.asaasApi.AsaasApi.service.AsaasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asaas")
public class AsaasController {

    private final AsaasService asaasService;
    public AsaasController(AsaasService asaasService) {
        this.asaasService = asaasService;
    }

    @GetMapping("/relatorio")
    public List<ClienteCobranca> relatorioCobrancas(@RequestParam(required = false) Status status,
                                                    @RequestParam(defaultValue = "VENCIMENTO") CampDate campoDate,
                                                    @RequestParam(required = false) LocalDate de,
                                                    @RequestParam(required = false)  LocalDate ate,
                                                    @RequestParam(required = false)  String nome,
                                                    @RequestParam(required = false)  String email ) {
        return asaasService.relatorioCobrancas(status, campoDate,de, ate, nome, email);
    }

    @GetMapping("/melhores-pagadores")
    public List<MelhoresPagadoresDTO>melhores(@RequestParam(required = false)  LocalDate de,
                                              @RequestParam(required = false)  LocalDate ate){
        return asaasService.melhoresPagadores(de, ate);
    }
}
