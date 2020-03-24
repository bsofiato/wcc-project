package com.matera.wcc.projeto.domain;


import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "VEICULO")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
public abstract class Veiculo {
    @Column(name = "ID")
    @Id
    @Type(type="uuid-char")
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "MARCA")
    private String marca;

    @Column(name = "MODELO")
    private String modelo;

    @Column(name = "ANO_MODELO")
    private Long anoModelo;

    @Column(name = "ANO_FABRICACAO")
    private Long anoFabricacao;

    @Column(name = "COMBUSTIVEL")
    @Enumerated(EnumType.STRING)
    private Combustivel combustivel;

    @Column(name = "IMPORTADO")
    private boolean importado;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() { return modelo; }

    public void setModelo(String modelo) { this.modelo = modelo; }

    public Long getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(Long anoModelo) {
        this.anoModelo = anoModelo;
    }

    public Long getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Long anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public Combustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(Combustivel combustivel) {
        this.combustivel = combustivel;
    }

    public boolean isImportado() { return importado; }

    public void setImportado(boolean importado) { this.importado = importado; }
}
