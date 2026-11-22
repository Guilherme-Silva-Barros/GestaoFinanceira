package com.asaasApi.AsaasApi.controller;

import com.asaasApi.AsaasApi.dto.ClienteCobranca;
import com.asaasApi.AsaasApi.enuns.Status;
import com.asaasApi.AsaasApi.service.AsaasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/asaas")
public class AssasController {

    private final AsaasService asaasService;
    public AssasController(AsaasService asaasService) {
        this.asaasService = asaasService;
    }

    @GetMapping
    public List<ClienteCobranca> relatorioCobrancas(@RequestParam Status status,
                                                    @RequestParam LocalDate de,
                                                    @RequestParam LocalDate ate,
                                                    @RequestParam String nome,
                                                    @RequestParam String email ) {
        return asaasService.relatorioCobrancas(status, de, ate, nome, email);
    }
}
