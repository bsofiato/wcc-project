package com.matera.wcc.projeto.repository;

import com.matera.wcc.projeto.domain.Caminhao;
import com.matera.wcc.projeto.domain.Carro;
import com.matera.wcc.projeto.domain.Moto;
import com.matera.wcc.projeto.domain.Veiculo;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
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

        Moto motoCarregada = saveAndLoad(moto);
        assertThat(motoCarregada.getId()).isNotNull();
        assertThat(motoCarregada.getMarca()).isEqualTo("HONDA");
    }

    @Test
    public void createCarro() {
        Carro carro = new Carro();
        carro.setMarca("VOLKSWAGEN");

        Carro carroCarregado = saveAndLoad(carro);
        assertThat(carroCarregado.getId()).isNotNull();
        assertThat(carroCarregado.getMarca()).isEqualTo("VOLKSWAGEN");
    }

    @Test
    public void createCaminhao() {
        Caminhao caminhao = new Caminhao();
        caminhao.setMarca("MERCEDEZ-BENZ");

        Caminhao carroCarregado = saveAndLoad(caminhao);
        assertThat(carroCarregado.getId()).isNotNull();
        assertThat(carroCarregado.getMarca()).isEqualTo("MERCEDEZ-BENZ");
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

    // Update

    @Test
    public void update() {
        Veiculo veiculo = loadCarro();
        veiculo.setMarca("Nova marca");
        this.repository.saveAndFlush(veiculo);

        assertThat(loadCarro().getMarca()).isEqualTo("Nova marca");
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
    }

    private void assertMotoSucessfullyLoaded(Moto moto) {
        assertThat(moto).isInstanceOf(Moto.class);
        assertThat(moto.getId()).isEqualTo(MOTO_ID);
        assertThat(moto.getMarca()).isEqualTo("YAMAHA");
    }

    private void assertCaminhaoSucessfullyLoaded(Caminhao caminhao) {
        assertThat(caminhao).isInstanceOf(Caminhao.class);
        assertThat(caminhao.getId()).isEqualTo(CAMINHAO_ID);
        assertThat(caminhao.getMarca()).isEqualTo("SCANIA");
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