package com.matera.wcc.projeto.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CARRO")
public class Carro extends Veiculo {
    @Column(name = "NUMERO_PORTAS")
    private Long numeroPortas;

    public Long getNumeroPortas() {
        return numeroPortas;
    }

    public void setNumeroPortas(Long numeroPortas) {
        this.numeroPortas = numeroPortas;
    }
}
