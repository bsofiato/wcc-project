package com.matera.wcc.projeto.rest;

import com.matera.wcc.projeto.service.VeiculoNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class ExceptionHandling implements ProblemHandling, SecurityAdviceTrait {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Problem> handleVeiculoNaoEncontrado(VeiculoNaoEncontradoException ex, NativeWebRequest request) {
        LOGGER.warn("Veiculo de id {} nao foi encontrado. Retornando 404", ex.getId());
        return create(Status.NOT_FOUND, ex, request);
    }
}
