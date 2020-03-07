package com.matera.wcc.projeto.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @todo colocar <i>numero de portas</i>
 */
@Entity
@DiscriminatorValue("CARRO")
public class Carro extends Veiculo {
}
