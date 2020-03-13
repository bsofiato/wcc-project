package com.matera.wcc.projeto.service;

import java.util.UUID;

public class VeiculoNaoEncontradoException extends Exception {
    private final UUID id;

    public VeiculoNaoEncontradoException(UUID id) {
        super("Veiculo de id  " + id.toString() + " nao encontrado");
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
