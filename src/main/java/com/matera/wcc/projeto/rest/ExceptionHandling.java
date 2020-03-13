package com.matera.wcc.projeto.rest;

import com.matera.wcc.projeto.service.VeiculoNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class ExceptionHandling implements ProblemHandling {

    @ExceptionHandler
    public ResponseEntity<Problem> handleVeiculoNaoEncontrado(VeiculoNaoEncontradoException ex, NativeWebRequest request) {
        return create(Status.NOT_FOUND, ex, request);
    }
}
