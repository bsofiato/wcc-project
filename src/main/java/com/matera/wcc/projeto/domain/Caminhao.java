package com.matera.wcc.projeto.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CAMINHAO")
public class Caminhao extends Veiculo {
}
