package com.matera.wcc.projeto.rest;

import com.matera.wcc.projeto.config.ModelMapperConfiguration;
import com.matera.wcc.projeto.domain.*;
import com.matera.wcc.projeto.service.VeiculoService;
import org.hamcrest.collection.IsEmptyCollection;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import({
    ModelMapperConfiguration.class,
    VeiculosApiDelegateImpl.class
})
public class VeiculosApiDelegateTest {

    private static final UUID VEICULO_ID = UUID.fromString("fe5c20ff-c863-407a-891d-e9fef61d3114");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VeiculoService service;

    @Test
    public void findAllEmpty() throws Exception {
        doReturn(Page.empty()).when(service).findAll(PageRequest.of(0, 10));

        mockMvc.perform(findAllRequest(0, 10))
            .andExpect(status().isOk())
            .andExpect(header().longValue("X-Total-Count", 0L))
            .andExpect(jsonPath("$").value(empty()));

        verify(service, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    public void findAll() throws Exception {
        doReturn(fullPage(0, 10)).when(service).findAll(PageRequest.of(0, 10));

        mockMvc.perform(findAllRequest(0, 10))
                .andExpect(status().isOk())
                .andExpect(header().longValue("X-Total-Count", 100L))
                .andExpect(jsonPath("$").value(hasSize(3)))
                .andExpect(jsonPath("$[0].tipo").value("carro"))
                .andExpect(jsonPath("$[0].id").value("6bf65456-072c-489c-bc48-3f4130b774b3"))
                .andExpect(jsonPath("$[0].modelo").value("GOL"))
                .andExpect(jsonPath("$[0].marca").value("VOLKSWAGEN"))
                .andExpect(jsonPath("$[0].anoFabricacao").value(2018))
                .andExpect(jsonPath("$[0].anoModelo").value(2019))
                .andExpect(jsonPath("$[0].combustivel").value("ALCOOL"))
                .andExpect(jsonPath("$[0].numeroPortas").value(5))
                .andExpect(jsonPath("$[1].tipo").value("moto"))
                .andExpect(jsonPath("$[1].id").value("c6cea841-0040-4c8a-8dab-a6aca8bbcd4c"))
                .andExpect(jsonPath("$[1].modelo").value("CC225"))
                .andExpect(jsonPath("$[1].marca").value("HONDA"))
                .andExpect(jsonPath("$[1].anoFabricacao").value(2015))
                .andExpect(jsonPath("$[1].anoModelo").value(2016))
                .andExpect(jsonPath("$[1].combustivel").value("GASOLINA"))
                .andExpect(jsonPath("$[2].tipo").value("caminhao"))
                .andExpect(jsonPath("$[2].id").value("6caf886a-7fc8-4adc-805c-737e47aee447"))
                .andExpect(jsonPath("$[2].modelo").value("AXOR"))
                .andExpect(jsonPath("$[2].marca").value("MERCEDEZ-BENZ"))
                .andExpect(jsonPath("$[2].anoFabricacao").value("2010"))
                .andExpect(jsonPath("$[2].anoModelo").value("2011"))
                .andExpect(jsonPath("$[2].combustivel").value("DIESEL"));

        verify(service, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    public void findNotFound() throws Exception {
        doReturn(Optional.empty()).when(service).findById(VEICULO_ID);
        mockMvc.perform(findOneRequest())
                .andExpect(status().isNotFound());

        verify(service, times(1)).findById(VEICULO_ID);
    }

    @Test
    public void findMoto() throws Exception {
        doReturn(Optional.of(moto())).when(service).findById(VEICULO_ID);
        mockMvc.perform(findOneRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("moto"))
                .andExpect(jsonPath("$.id").value("c6cea841-0040-4c8a-8dab-a6aca8bbcd4c"))
                .andExpect(jsonPath("$.modelo").value("CC225"))
                .andExpect(jsonPath("$.marca").value("HONDA"))
                .andExpect(jsonPath("$.anoFabricacao").value(2015))
                .andExpect(jsonPath("$.anoModelo").value(2016))
                .andExpect(jsonPath("$.combustivel").value("GASOLINA"));

        verify(service, times(1)).findById(VEICULO_ID);
    }

    @Test
    public void findCarro() throws Exception {
        doReturn(Optional.of(carro())).when(service).findById(VEICULO_ID);
        mockMvc.perform(findOneRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("carro"))
                .andExpect(jsonPath("$.id").value("6bf65456-072c-489c-bc48-3f4130b774b3"))
                .andExpect(jsonPath("$.modelo").value("GOL"))
                .andExpect(jsonPath("$.marca").value("VOLKSWAGEN"))
                .andExpect(jsonPath("$.anoFabricacao").value(2018))
                .andExpect(jsonPath("$.anoModelo").value(2019))
                .andExpect(jsonPath("$.combustivel").value("ALCOOL"))
                .andExpect(jsonPath("$.numeroPortas").value(5));

        verify(service, times(1)).findById(VEICULO_ID);
    }

    @Test
    public void findCaminhao() throws Exception {
        doReturn(Optional.of(caminhao())).when(service).findById(VEICULO_ID);
        mockMvc.perform(findOneRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("caminhao"))
                .andExpect(jsonPath("$.id").value("6caf886a-7fc8-4adc-805c-737e47aee447"))
                .andExpect(jsonPath("$.modelo").value("AXOR"))
                .andExpect(jsonPath("$.marca").value("MERCEDEZ-BENZ"))
                .andExpect(jsonPath("$.anoFabricacao").value("2010"))
                .andExpect(jsonPath("$.anoModelo").value("2011"))
                .andExpect(jsonPath("$.combustivel").value("DIESEL"));

        verify(service, times(1)).findById(VEICULO_ID);
    }

    private MockHttpServletRequestBuilder findOneRequest() {
        return get("/v1/veiculos/{id}", VEICULO_ID);
    }

    private MockHttpServletRequestBuilder findAllRequest(int page, int size) {
        return get("/v1/veiculos").param("page", String.valueOf(page)).param("size", String.valueOf(size));
    }

    private Page<Veiculo> fullPage(int page, int size) {
        List<Veiculo> content = new ArrayList<>();
        content.add(carro());
        content.add(moto());
        content.add(caminhao());
        return new PageImpl<>(content, PageRequest.of(page, size), 100);
    }

    public Moto moto() {
        Moto moto = new Moto();
        moto.setId(UUID.fromString("c6cea841-0040-4c8a-8dab-a6aca8bbcd4c"));
        moto.setMarca("HONDA");
        moto.setModelo("CC225");
        moto.setAnoFabricacao(2015L);
        moto.setAnoModelo(2016L);
        moto.setCombustivel(Combustivel.GASOLINA);
        return moto;
    }

    public Carro carro() {
        Carro carro = new Carro();
        carro.setId(UUID.fromString("6bf65456-072c-489c-bc48-3f4130b774b3"));
        carro.setMarca("VOLKSWAGEN");
        carro.setModelo("GOL");
        carro.setAnoFabricacao(2018L);
        carro.setAnoModelo(2019L);
        carro.setCombustivel(Combustivel.ALCOOL);
        carro.setNumeroPortas(5L);

        return carro;
    }

    public Caminhao caminhao() {
        Caminhao caminhao = new Caminhao();
        caminhao.setId(UUID.fromString("6caf886a-7fc8-4adc-805c-737e47aee447"));
        caminhao.setMarca("MERCEDEZ-BENZ");
        caminhao.setModelo("AXOR");
        caminhao.setAnoFabricacao(2010L);
        caminhao.setAnoModelo(2011L);
        caminhao.setCombustivel(Combustivel.DIESEL);

        return caminhao;
    }
}