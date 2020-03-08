package com.matera.wcc.projeto.repository;

import com.matera.wcc.projeto.domain.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("veiculo-repository-fixture.sql")
public class VeiculoRepositoryTest {
    private static final UUID MOTO_ID = UUID.fromString("a9b55239-6665-46f2-99be-16a4f2462328");
    private static final UUID CAMINHAO_ID = UUID.fromString("3b5e78ea-acfc-4bd9-b238-c9cefeaaaa66");
    private static final UUID CARRO_ID = UUID.fromString("7ec159be-c5fb-41ff-8cc5-393033518859");

    @Autowired
    private VeiculoRepository repository;

    // Create

    @Test
    public void createMoto() {
        Moto moto = new Moto();
        moto.setMarca("HONDA");
        moto.setModelo("CC225");
        moto.setAnoFabricacao(2015L);
        moto.setAnoModelo(2016L);
        moto.setCombustivel(Combustivel.GASOLINA);

        Moto motoCarregada = saveAndLoad(moto);
        assertThat(motoCarregada.getId()).isNotNull();
        assertThat(motoCarregada.getMarca()).isEqualTo("HONDA");
        assertThat(motoCarregada.getModelo()).isEqualTo("CC225");
        assertThat(motoCarregada.getAnoFabricacao()).isEqualTo(2015L);
        assertThat(motoCarregada.getAnoModelo()).isEqualTo(2016L);
        assertThat(motoCarregada.getCombustivel()).isSameAs(Combustivel.GASOLINA);
    }

    @Test
    public void createCarro() {
        Carro carro = new Carro();
        carro.setMarca("VOLKSWAGEN");
        carro.setModelo("GOL");
        carro.setAnoFabricacao(2018L);
        carro.setAnoModelo(2019L);
        carro.setCombustivel(Combustivel.ALCOOL);
        carro.setNumeroPortas(5L);

        Carro carroCarregado = saveAndLoad(carro);
        assertThat(carroCarregado.getId()).isNotNull();
        assertThat(carroCarregado.getMarca()).isEqualTo("VOLKSWAGEN");
        assertThat(carroCarregado.getModelo()).isEqualTo("GOL");
        assertThat(carroCarregado.getAnoFabricacao()).isEqualTo(2018L);
        assertThat(carroCarregado.getAnoModelo()).isEqualTo(2019L);
        assertThat(carroCarregado.getCombustivel()).isSameAs(Combustivel.ALCOOL);
        assertThat(carroCarregado.getNumeroPortas()).isEqualTo(5L);
    }

    @Test
    public void createCaminhao() {
        Caminhao caminhao = new Caminhao();
        caminhao.setMarca("MERCEDEZ-BENZ");
        caminhao.setModelo("AXOR");
        caminhao.setAnoFabricacao(2010L);
        caminhao.setAnoModelo(2011L);
        caminhao.setCombustivel(Combustivel.DIESEL);

        Caminhao carroCarregado = saveAndLoad(caminhao);
        assertThat(carroCarregado.getId()).isNotNull();
        assertThat(carroCarregado.getMarca()).isEqualTo("MERCEDEZ-BENZ");
        assertThat(carroCarregado.getModelo()).isEqualTo("AXOR");
        assertThat(carroCarregado.getAnoFabricacao()).isEqualTo(2010L);
        assertThat(carroCarregado.getAnoModelo()).isEqualTo(2011L);
        assertThat(carroCarregado.getCombustivel()).isSameAs(Combustivel.DIESEL);
    }

    // Read

    @Test
    public void findMoto() {
        assertMotoSucessfullyLoaded(loadMoto());
    }

    @Test
    public void findCaminhao() {
        assertCaminhaoSucessfullyLoaded(loadCaminhao());
    }

    @Test
    public void findCarro() {
        assertCarroSucessfullyLoaded(loadCarro());
    }

    @Test
    public void notFound() {
        assertThat(this.repository.findById(UUID.randomUUID())).isEmpty();
    }


    @Test
    public void findAll() {
        List<Veiculo> veiculos = this.repository.findAll();

        assertThat(veiculos).hasSize(3);
        assertCaminhaoSucessfullyLoaded((Caminhao) veiculos.get(0));
        assertCarroSucessfullyLoaded((Carro) veiculos.get(1));
        assertMotoSucessfullyLoaded((Moto) veiculos.get(2));
    }


    @Test
    public void findAllPaginado() {
        Pageable request = PageRequest.of(2, 1);

        Page<Veiculo> veiculos = this.repository.findAll(request);

        assertThat(veiculos.getTotalElements()).isEqualTo(3);
        assertThat(veiculos.getContent()).hasSize(1);
        assertMotoSucessfullyLoaded((Moto) veiculos.getContent().get(0));
    }

    @Test
    public void findAllPaginadoPaginaInexistente() {
        Pageable request = PageRequest.of(3, 1);

        Page<Veiculo> veiculos = this.repository.findAll(request);
        assertThat(veiculos.getTotalElements()).isEqualTo(3);
        assertThat(veiculos.getContent()).isEmpty();
    }

    // Update

    @Test
    public void update() {
        Carro veiculo = loadCarro();
        veiculo.setMarca("Nova marca");
        veiculo.setModelo("Novo modelo");
        veiculo.setAnoFabricacao(2020L);
        veiculo.setAnoModelo(2021L);
        veiculo.setCombustivel(Combustivel.ALCOOL);
        this.repository.saveAndFlush(veiculo);

        assertThat(loadCarro().getMarca()).isEqualTo("Nova marca");
        assertThat(loadCarro().getModelo()).isEqualTo("Novo modelo");
        assertThat(loadCarro().getAnoModelo()).isEqualTo(2021L);
        assertThat(loadCarro().getAnoFabricacao()).isEqualTo(2020L);
        assertThat(loadCarro().getCombustivel()).isSameAs(Combustivel.ALCOOL);
    }

    // Delete

    @Test
    public void delete() {
        this.repository.deleteById(CARRO_ID);
        assertThat(this.repository.findById(CARRO_ID)).isEmpty();
    }

    private void assertCarroSucessfullyLoaded(Carro carro) {
        assertThat(carro).isInstanceOf(Carro.class);
        assertThat(carro.getId()).isEqualTo(CARRO_ID);
        assertThat(carro.getMarca()).isEqualTo("FORD");
        assertThat(carro.getModelo()).isEqualTo("KA");
        assertThat(carro.getAnoFabricacao()).isEqualTo(2019L);
        assertThat(carro.getAnoModelo()).isEqualTo(2020L);
        assertThat(carro.getCombustivel()).isSameAs(Combustivel.FLEX);
        assertThat(carro.getNumeroPortas()).isEqualTo(4);
    }

    private void assertMotoSucessfullyLoaded(Moto moto) {
        assertThat(moto).isInstanceOf(Moto.class);
        assertThat(moto.getId()).isEqualTo(MOTO_ID);
        assertThat(moto.getMarca()).isEqualTo("YAMAHA");
        assertThat(moto.getModelo()).isEqualTo("CC125");
        assertThat(moto.getAnoFabricacao()).isEqualTo(2018L);
        assertThat(moto.getAnoModelo()).isEqualTo(2018L);
        assertThat(moto.getCombustivel()).isSameAs(Combustivel.GASOLINA);
    }

    private void assertCaminhaoSucessfullyLoaded(Caminhao caminhao) {
        assertThat(caminhao).isInstanceOf(Caminhao.class);
        assertThat(caminhao.getId()).isEqualTo(CAMINHAO_ID);
        assertThat(caminhao.getMarca()).isEqualTo("SCANIA");
        assertThat(caminhao.getModelo()).isEqualTo("SCANIA");
        assertThat(caminhao.getAnoFabricacao()).isEqualTo(2020L);
        assertThat(caminhao.getAnoModelo()).isEqualTo(2020L);
        assertThat(caminhao.getCombustivel()).isSameAs(Combustivel.DIESEL);
    }

    private <T extends Veiculo> T saveAndLoad(T veiculo) {
        veiculo = this.repository.saveAndFlush(veiculo);
        return (T)(this.repository.findById(veiculo.getId()).get());
    }

    @NotNull
    private Carro loadCarro() {
        return (Carro) this.repository.findById(CARRO_ID).get();
    }

    @NotNull
    private Moto loadMoto() {
        return (Moto) this.repository.findById(MOTO_ID).get();
    }

    @NotNull
    private Caminhao loadCaminhao() {
        return (Caminhao) this.repository.findById(CAMINHAO_ID).get();
    }
}