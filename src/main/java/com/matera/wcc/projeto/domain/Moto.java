package com.matera.wcc.projeto.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MOTO")
public class Moto extends Veiculo {
}
