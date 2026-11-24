package com.asaasApi.AsaasApi.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;

@RestControllerAdvice
public class AsaasHandlerExceptions extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AsaasHandlerExceptions.class);

    @ExceptionHandler(ParametroInvalidoException.class)
    ProblemDetail parametroInvalido(ParametroInvalidoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResourceAccessException.class)
    ProblemDetail resourceAccess(ResourceAccessException ex) {
        log.error("Falha de comunicação com o Asaas", ex);
        boolean timeout = ex.getCause() instanceof SocketTimeoutException
                || ex.getCause() instanceof HttpTimeoutException;
        return timeout
                ? ProblemDetail.forStatusAndDetail(HttpStatus.GATEWAY_TIMEOUT,
                "O serviço de cobranças demorou demais para responder.")
                : ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE,
                "Não foi possível conectar ao serviço de cobranças.");
    }

    @ExceptionHandler(RestClientResponseException.class)
    ProblemDetail asaasErro(RestClientResponseException ex) {
        int s = ex.getStatusCode().value();
        log.error("Asaas respondeu {}: {}", s, truncar(ex.getResponseBodyAsString()));
        if (s == 429) {
            return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE,
                    "Limite de requisições atingido. Tente novamente em instantes.");
        }
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY,
                "O serviço de cobranças não atendeu a requisição.");
    }

    @ExceptionHandler(RestClientException.class)
    ProblemDetail respostaInesperada(RestClientException ex) {
        log.error("Resposta inesperada do Asaas", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY,
                "O serviço de cobranças devolveu uma resposta inválida.");
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail InternalException(Exception ex) {
        log.error("Erro inesperado", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado.");
    }

    private static String truncar(String s) {
        return s == null ? "" : s.substring(0, Math.min(s.length(), 500));
    }
}
