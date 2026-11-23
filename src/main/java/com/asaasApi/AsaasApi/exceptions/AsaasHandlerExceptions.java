package com.asaasApi.AsaasApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AsaasHandlerExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ParametroInvalidoException.class)
    ProblemDetail parametroInvalido(ParametroInvalidoException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Parâmetro inválido "+ex.getMessage());
    }

    @ExceptionHandler(ResourceAccessException.class)
    ProblemDetail resourceAccess(ResourceAccessException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.GATEWAY_TIMEOUT, "Erro com a API do Asaas "+ ex.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    ProblemDetail respostaInesperada(RestClientException ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, "O serviço devolveu uma resposta inválida "+ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    ProblemDetail InternalException(Exception ex){
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno " +ex.getMessage());
    }
}
