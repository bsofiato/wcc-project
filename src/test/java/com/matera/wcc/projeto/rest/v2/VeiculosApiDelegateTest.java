package com.matera.wcc.projeto.rest.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matera.wcc.projeto.config.ErrorHandlingConfiguration;
import com.matera.wcc.projeto.config.ModelMapperConfiguration;
import com.matera.wcc.projeto.config.ResourceServerConfiguration;
import com.matera.wcc.projeto.domain.*;
import com.matera.wcc.projeto.persona.LoggedAsJessica;
import com.matera.wcc.projeto.rest.v2.dto.*;
import com.matera.wcc.projeto.service.VeiculoNaoEncontradoException;
import com.matera.wcc.projeto.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest()
@Import({
        ErrorHandlingConfiguration.class,
        ModelMapperConfiguration.class,
        ResourceServerConfiguration.class,
        V2VeiculosApiDelegateImpl.class,
        org.zalando.problem.spring.web.autoconfigure.security.SecurityConfiguration.class,
        com.matera.wcc.projeto.config.SecurityConfiguration.class
})
@LoggedAsJessica
public class VeiculosApiDelegateTest {

    private static final UUID VEICULO_ID = UUID.fromString("fe5c20ff-c863-407a-891d-e9fef61d3114");
    private static final String INVALID_MODELO = "AHBHUSUAHUADGYAGYDAGDYUUHSDUHIAUSHIAHUIDYADGYADYUSHAHUSDUIADHUIHAUIDHUDAHUIDAHUIAUHDHUADSIUDYGADGYUADYUHUIDAHUDAHUD";
    private static final String INVALID_MARCA = "AHBHUSUAHUADGYAGYDAGDYUUHSDUHIAUSHIAHUIDYADGYADYUSHAHUSDUIADHUIHAUIDHUDAHUIDAHUIAUHDHUADSIUDYGADGYUADYUHUIDAHUDAHUD";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VeiculoService service;

    @Captor
    private ArgumentCaptor<Veiculo> veiculoSalvo;


    @ParameterizedTest(name = "O resultado da inserção {1} deve 400 - Bad Request")
    @MethodSource("invalidVehicles")
    public void createBadRequest(VeiculoDTO veiculo) throws Exception {
        mockMvc.perform(createRequest(veiculo)).andExpect(status().isBadRequest());
    }

    @Test
    public void createCarro() throws Exception {

        doReturn(VEICULO_ID).when(service).insert(isA(Carro.class));

        mockMvc.perform(createRequest(carroDTO()))
                .andExpect(status().isCreated())
                .andExpect(locationToPointToNewlyCreatedVeiculo());

        verify(service, times(1)).insert(veiculoSalvo.capture());

        assertThat(veiculoSalvo.getValue()).isInstanceOf(Carro.class);
        assertThat(veiculoSalvo.getValue().getAnoFabricacao()).isEqualTo(2018);
        assertThat(veiculoSalvo.getValue().getAnoModelo()).isEqualTo(2019);
        assertThat(veiculoSalvo.getValue().getCombustivel()).isSameAs(Combustivel.ALCOOL);
        assertThat(veiculoSalvo.getValue().getModelo()).isEqualTo("GOL");
        assertThat(veiculoSalvo.getValue().getMarca()).isEqualTo("VOLKSWAGEN");
        assertThat(veiculoSalvo.getValue().isImportado()).isTrue();
        assertThat(veiculoSalvo.getValue()).extracting("numeroPortas").isEqualTo(5L);
    }

    @Test
    public void createMoto() throws Exception {
        doReturn(VEICULO_ID).when(service).insert(isA(Moto.class));

        mockMvc.perform(createRequest(motoDTO()))
                .andExpect(status().isCreated())
                .andExpect(locationToPointToNewlyCreatedVeiculo());

        verify(service, times(1)).insert(veiculoSalvo.capture());

        assertThat(veiculoSalvo.getValue()).isInstanceOf(Moto.class);
        assertThat(veiculoSalvo.getValue().getAnoFabricacao()).isEqualTo(2015);
        assertThat(veiculoSalvo.getValue().getAnoModelo()).isEqualTo(2016);
        assertThat(veiculoSalvo.getValue().getCombustivel()).isSameAs(Combustivel.GASOLINA);
        assertThat(veiculoSalvo.getValue().getModelo()).isEqualTo("CC225");
        assertThat(veiculoSalvo.getValue().getMarca()).isEqualTo("HONDA");
        assertThat(veiculoSalvo.getValue().isImportado()).isFalse();
    }

    @Test
    public void createCaminhao() throws Exception {
        doReturn(VEICULO_ID).when(service).insert(isA(Caminhao.class));

        mockMvc.perform(createRequest(caminhaoDTO()))
                .andExpect(status().isCreated())
                .andExpect(locationToPointToNewlyCreatedVeiculo());

        verify(service, times(1)).insert(veiculoSalvo.capture());

        assertThat(veiculoSalvo.getValue()).isInstanceOf(Caminhao.class);
        assertThat(veiculoSalvo.getValue().getAnoFabricacao()).isEqualTo(2010);
        assertThat(veiculoSalvo.getValue().getAnoModelo()).isEqualTo(2011);
        assertThat(veiculoSalvo.getValue().getCombustivel()).isSameAs(Combustivel.DIESEL);
        assertThat(veiculoSalvo.getValue().getModelo()).isEqualTo("AXOR");
        assertThat(veiculoSalvo.getValue().getMarca()).isEqualTo("MERCEDEZ-BENZ");
        assertThat(veiculoSalvo.getValue().isImportado()).isTrue();
    }

    @ParameterizedTest(name = "O resultado da chamada de busca de veiculos com os parametros page={0} e size={1} deve ser 400 (Bad request)")
    @CsvSource({
            ",10",
            "0,",
            "-1, 10",
            "0, -1",
            "0, 101",
            "0, 0"
    })
    public void findAllBadRequest(Integer page, Integer size) throws Exception {
        mockMvc.perform(findAllRequest(page, size)).andExpect(status().isBadRequest());
    }


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
                .andExpect(jsonPath("$[0].importado").value(true))
                .andExpect(jsonPath("$[1].tipo").value("moto"))
                .andExpect(jsonPath("$[1].id").value("c6cea841-0040-4c8a-8dab-a6aca8bbcd4c"))
                .andExpect(jsonPath("$[1].modelo").value("CC225"))
                .andExpect(jsonPath("$[1].marca").value("HONDA"))
                .andExpect(jsonPath("$[1].anoFabricacao").value(2015))
                .andExpect(jsonPath("$[1].anoModelo").value(2016))
                .andExpect(jsonPath("$[1].combustivel").value("GASOLINA"))
                .andExpect(jsonPath("$[1].importado").value(false))
                .andExpect(jsonPath("$[2].tipo").value("caminhao"))
                .andExpect(jsonPath("$[2].id").value("6caf886a-7fc8-4adc-805c-737e47aee447"))
                .andExpect(jsonPath("$[2].modelo").value("AXOR"))
                .andExpect(jsonPath("$[2].marca").value("MERCEDEZ-BENZ"))
                .andExpect(jsonPath("$[2].anoFabricacao").value("2010"))
                .andExpect(jsonPath("$[2].anoModelo").value("2011"))
                .andExpect(jsonPath("$[2].combustivel").value("DIESEL"))
                .andExpect(jsonPath("$[2].importado").value(true));


        verify(service, times(1)).findAll(PageRequest.of(0, 10));
    }

    @Test
    public void findNotFound() throws Exception {
        doThrow(veiculoNaoEncontradoException()).when(service).findById(VEICULO_ID);

        mockMvc.perform(findOneRequest()).andExpect(status().isNotFound());

        verify(service, times(1)).findById(VEICULO_ID);
    }

    @Test
    public void findMoto() throws Exception {
        doReturn(moto()).when(service).findById(VEICULO_ID);
        mockMvc.perform(findOneRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("moto"))
                .andExpect(jsonPath("$.id").value("c6cea841-0040-4c8a-8dab-a6aca8bbcd4c"))
                .andExpect(jsonPath("$.modelo").value("CC225"))
                .andExpect(jsonPath("$.marca").value("HONDA"))
                .andExpect(jsonPath("$.anoFabricacao").value(2015))
                .andExpect(jsonPath("$.anoModelo").value(2016))
                .andExpect(jsonPath("$.importado").value(false))
                .andExpect(jsonPath("$.combustivel").value("GASOLINA"));

        verify(service, times(1)).findById(VEICULO_ID);
    }

    @Test
    public void findCarro() throws Exception {
        doReturn(carro()).when(service).findById(VEICULO_ID);
        mockMvc.perform(findOneRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("carro"))
                .andExpect(jsonPath("$.id").value("6bf65456-072c-489c-bc48-3f4130b774b3"))
                .andExpect(jsonPath("$.modelo").value("GOL"))
                .andExpect(jsonPath("$.marca").value("VOLKSWAGEN"))
                .andExpect(jsonPath("$.anoFabricacao").value(2018))
                .andExpect(jsonPath("$.anoModelo").value(2019))
                .andExpect(jsonPath("$.combustivel").value("ALCOOL"))
                .andExpect(jsonPath("$.importado").value(true))
                .andExpect(jsonPath("$.numeroPortas").value(5));

        verify(service, times(1)).findById(VEICULO_ID);
    }

    @Test
    public void findCaminhao() throws Exception {
        doReturn(caminhao()).when(service).findById(VEICULO_ID);
        mockMvc.perform(findOneRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("caminhao"))
                .andExpect(jsonPath("$.id").value("6caf886a-7fc8-4adc-805c-737e47aee447"))
                .andExpect(jsonPath("$.modelo").value("AXOR"))
                .andExpect(jsonPath("$.marca").value("MERCEDEZ-BENZ"))
                .andExpect(jsonPath("$.anoFabricacao").value("2010"))
                .andExpect(jsonPath("$.anoModelo").value("2011"))
                .andExpect(jsonPath("$.importado").value(true))
                .andExpect(jsonPath("$.combustivel").value("DIESEL"));

        verify(service, times(1)).findById(VEICULO_ID);
    }

    @ParameterizedTest(name = "O resultado da inserção {1} deve 400 - Bad Request")
    @MethodSource("invalidVehicles")
    public void updateBadRequest(VeiculoDTO veiculo) throws Exception {
        mockMvc.perform(updateRequest(veiculo)).andExpect(status().isBadRequest());
    }


    @Test
    public void updateCarro() throws Exception {
        doNothing().when(service).update(isA(Carro.class));

        mockMvc.perform(updateRequest(carroDTO()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("carro"))
                .andExpect(jsonPath("$.id").value(VEICULO_ID.toString()))
                .andExpect(jsonPath("$.modelo").value("GOL"))
                .andExpect(jsonPath("$.marca").value("VOLKSWAGEN"))
                .andExpect(jsonPath("$.anoFabricacao").value(2018))
                .andExpect(jsonPath("$.anoModelo").value(2019))
                .andExpect(jsonPath("$.combustivel").value("ALCOOL"))
                .andExpect(jsonPath("$.importado").value(true))
                .andExpect(jsonPath("$.numeroPortas").value(5));

        verify(service, times(1)).update(veiculoSalvo.capture());

        assertThat(veiculoSalvo.getValue()).isInstanceOf(Carro.class);
        assertThat(veiculoSalvo.getValue().getId()).isEqualTo(VEICULO_ID);
        assertThat(veiculoSalvo.getValue().getAnoFabricacao()).isEqualTo(2018);
        assertThat(veiculoSalvo.getValue().getAnoModelo()).isEqualTo(2019);
        assertThat(veiculoSalvo.getValue().getCombustivel()).isSameAs(Combustivel.ALCOOL);
        assertThat(veiculoSalvo.getValue().getModelo()).isEqualTo("GOL");
        assertThat(veiculoSalvo.getValue().getMarca()).isEqualTo("VOLKSWAGEN");
        assertThat(veiculoSalvo.getValue().isImportado()).isTrue();
        assertThat(veiculoSalvo.getValue()).extracting("numeroPortas").isEqualTo(5L);
    }

    @Test
    public void updateMoto() throws Exception {
        doNothing().when(service).update(isA(Moto.class));

        mockMvc.perform(updateRequest(motoDTO()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("moto"))
                .andExpect(jsonPath("$.id").value(VEICULO_ID.toString()))
                .andExpect(jsonPath("$.modelo").value("CC225"))
                .andExpect(jsonPath("$.marca").value("HONDA"))
                .andExpect(jsonPath("$.anoFabricacao").value(2015))
                .andExpect(jsonPath("$.anoModelo").value(2016))
                .andExpect(jsonPath("$.importado").value(false))
                .andExpect(jsonPath("$.combustivel").value("GASOLINA"));

        verify(service, times(1)).update(veiculoSalvo.capture());

        assertThat(veiculoSalvo.getValue()).isInstanceOf(Moto.class);
        assertThat(veiculoSalvo.getValue().getId()).isEqualTo(VEICULO_ID);
        assertThat(veiculoSalvo.getValue().getAnoFabricacao()).isEqualTo(2015);
        assertThat(veiculoSalvo.getValue().getAnoModelo()).isEqualTo(2016);
        assertThat(veiculoSalvo.getValue().getCombustivel()).isSameAs(Combustivel.GASOLINA);
        assertThat(veiculoSalvo.getValue().getModelo()).isEqualTo("CC225");
        assertThat(veiculoSalvo.getValue().getMarca()).isEqualTo("HONDA");
        assertThat(veiculoSalvo.getValue().isImportado()).isFalse();
    }

    @Test
    public void updateCaminhao() throws Exception {
        doNothing().when(service).update(isA(Caminhao.class));

        mockMvc.perform(updateRequest(caminhaoDTO()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("caminhao"))
                .andExpect(jsonPath("$.id").value(VEICULO_ID.toString()))
                .andExpect(jsonPath("$.modelo").value("AXOR"))
                .andExpect(jsonPath("$.marca").value("MERCEDEZ-BENZ"))
                .andExpect(jsonPath("$.anoFabricacao").value("2010"))
                .andExpect(jsonPath("$.anoModelo").value("2011"))
                .andExpect(jsonPath("$.importado").value(true))
                .andExpect(jsonPath("$.combustivel").value("DIESEL"));

        verify(service, times(1)).update(veiculoSalvo.capture());

        assertThat(veiculoSalvo.getValue()).isInstanceOf(Caminhao.class);
        assertThat(veiculoSalvo.getValue().getId()).isEqualTo(VEICULO_ID);
        assertThat(veiculoSalvo.getValue().getAnoFabricacao()).isEqualTo(2010);
        assertThat(veiculoSalvo.getValue().getAnoModelo()).isEqualTo(2011);
        assertThat(veiculoSalvo.getValue().getCombustivel()).isSameAs(Combustivel.DIESEL);
        assertThat(veiculoSalvo.getValue().getModelo()).isEqualTo("AXOR");
        assertThat(veiculoSalvo.getValue().getMarca()).isEqualTo("MERCEDEZ-BENZ");
        assertThat(veiculoSalvo.getValue().isImportado()).isTrue();
    }

    @Test
    public void updateVehicleNotFound() throws Exception {
        doThrow(veiculoNaoEncontradoException()).when(service).update(isA(Carro.class));

        mockMvc.perform(updateRequest(carroDTO()))
                .andExpect(status().isNotFound());

        verify(service, times(1)).update(veiculoSalvo.capture());
    }

    @Test
    public void deleteVehicle() throws Exception {
        doNothing().when(service).delete(VEICULO_ID);

        mockMvc.perform(deleteRequest())
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(VEICULO_ID);
    }


    @Test
    public void deleteVehicleNotFound() throws Exception {
        doThrow(veiculoNaoEncontradoException()).when(service).delete(VEICULO_ID);

        mockMvc.perform(deleteRequest())
                .andExpect(status().isNotFound());

        verify(service, times(1)).delete(VEICULO_ID);
    }


    private RequestBuilder deleteRequest() { return delete("/v2/veiculos/{id}", VEICULO_ID.toString()); }
    private MockHttpServletRequestBuilder findOneRequest() {
        return get("/v2/veiculos/{id}", VEICULO_ID);
    }
    private MockHttpServletRequestBuilder findAllRequest(Integer page, Integer size) {
        return get("/v2/veiculos")
                .param("page", (page == null) ? null : page.toString())
                .param("size", (size == null) ? null : size.toString());
    }

    private RequestBuilder createRequest(VeiculoDTO veiculoDTO)  throws Exception {
        return post("/v2/veiculos").contentType(MediaType.APPLICATION_JSON).content(toJson(veiculoDTO));
    }

    private RequestBuilder updateRequest(VeiculoDTO veiculoDTO) throws Exception {
        return put("/v2/veiculos/{id}", VEICULO_ID.toString()).contentType(MediaType.APPLICATION_JSON).content(toJson(veiculoDTO.id(VEICULO_ID)));
    }

    private String toJson(VeiculoDTO veiculoDTO) throws Exception {
        return this.mapper.writeValueAsString(veiculoDTO);
    }

    private ResultMatcher locationToPointToNewlyCreatedVeiculo() {
        return header().string("Location", "/v2/veiculos/" + VEICULO_ID.toString());
    }

    private Page<Veiculo> fullPage(int page, int size) {
        List<Veiculo> content = new ArrayList<>();
        content.add(carro());
        content.add(moto());
        content.add(caminhao());
        return new PageImpl<>(content, PageRequest.of(page, size), 100);
    }

    private Moto moto() {
        Moto moto = new Moto();
        moto.setId(UUID.fromString("c6cea841-0040-4c8a-8dab-a6aca8bbcd4c"));
        moto.setMarca("HONDA");
        moto.setModelo("CC225");
        moto.setAnoFabricacao(2015L);
        moto.setAnoModelo(2016L);
        moto.setCombustivel(Combustivel.GASOLINA);
        return moto;
    }

    private Carro carro() {
        Carro carro = new Carro();
        carro.setId(UUID.fromString("6bf65456-072c-489c-bc48-3f4130b774b3"));
        carro.setMarca("VOLKSWAGEN");
        carro.setModelo("GOL");
        carro.setAnoFabricacao(2018L);
        carro.setAnoModelo(2019L);
        carro.setCombustivel(Combustivel.ALCOOL);
        carro.setNumeroPortas(5L);
        carro.setImportado(true);

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
        caminhao.setImportado(true);

        return caminhao;
    }

    private static MotoDTO motoDTO() {
        MotoDTO moto = new MotoDTO();
        moto.setMarca("HONDA");
        moto.setModelo("CC225");
        moto.setAnoFabricacao(2015);
        moto.setAnoModelo(2016);
        moto.setCombustivel(CombustivelDTO.GASOLINA);
//        moto.setImportado(false); @todo descomentar

        return moto;
    }

    private static CarroDTO carroDTO() {
        CarroDTO carro = new CarroDTO();
        carro.setMarca("VOLKSWAGEN");
        carro.setModelo("GOL");
        carro.setAnoFabricacao(2018);
        carro.setAnoModelo(2019);
        carro.setCombustivel(CombustivelDTO.ALCOOL);
        carro.setNumeroPortas(5);
//        carro.setImportado(true); @todo descomentar
        return carro;
    }

    private static CaminhaoDTO caminhaoDTO() {
        CaminhaoDTO caminhao = new CaminhaoDTO();
        caminhao.setMarca("MERCEDEZ-BENZ");
        caminhao.setModelo("AXOR");
        caminhao.setAnoFabricacao(2010);
        caminhao.setAnoModelo(2011);
        caminhao.setCombustivel(CombustivelDTO.DIESEL);
//        caminhao.setImportado(true); @todo descomentar

        return caminhao;
    }

    private static Stream<Arguments> invalidVehicles() {
        return Stream.of(
//                Arguments.of(carroDTO().importado(null), "carro sem fornecer a flag de importado"),      @todo descomentar
//                Arguments.of(motoDTO().importado(null), "moto sem fornecer a flag de importado"),        @todo descomentar
//                Arguments.of(caminhaoDTO().importado(null), "caminhao sem fornecer a flag de importado") @todo descomentar
            Arguments.of(carroDTO().modelo(INVALID_MODELO), "carro cujo modelo tem tamanho maior que 80"),
            Arguments.of(motoDTO().modelo(INVALID_MODELO), "moto cujo modelo tem tamanho maior que 80"),
            Arguments.of(caminhaoDTO().modelo(INVALID_MODELO), "caminhão cujo modelo tem tamanho maior que 80"),
            Arguments.of(carroDTO().marca(INVALID_MARCA), "carro cuja marca tem tamanho maior que 80"),
            Arguments.of(motoDTO().marca(INVALID_MARCA), "moto cuja marca tem tamanho maior que 80"),
            Arguments.of(caminhaoDTO().marca(INVALID_MARCA), "caminhão cuja marca tem tamanho maior que 80"),
            Arguments.of(carroDTO().anoFabricacao(1899), "carro cujo ano de fabricacao é menor de 1900"),
            Arguments.of(motoDTO().anoFabricacao(1899), "moto cujo ano de fabricação é menor que 1900"),
            Arguments.of(caminhaoDTO().anoFabricacao(1899), "caminhão cujo ano de fabricação é menor que 1900"),
            Arguments.of(carroDTO().anoFabricacao(10000), "carro cujo ano de fabricacao é maior que 9999"),
            Arguments.of(motoDTO().anoFabricacao(10000), "moto cujo ano de fabricação é maior que 9999"),
            Arguments.of(caminhaoDTO().anoFabricacao(10000), "caminhão cujo ano de fabricação é maior que 9999"),
            Arguments.of(carroDTO().anoModelo(1899), "carro cujo ano de modelo é menor de 1900"),
            Arguments.of(motoDTO().anoModelo(1899), "moto cujo ano de modelo é menor que 1900"),
            Arguments.of(caminhaoDTO().anoModelo(1899), "caminhão cujo ano de modelo é menor que 1900"),
            Arguments.of(carroDTO().anoModelo(10000), "carro cujo ano de modelo é maior que 9999"),
            Arguments.of(motoDTO().anoModelo(10000), "moto cujo ano de modelo é maior que 9999"),
            Arguments.of(caminhaoDTO().anoModelo(10000), "caminhão cujo ano de modelo é maior que 9999"),
            Arguments.of(carroDTO().numeroPortas(0), "carro cujo numero de portas é menor que 1"),
            Arguments.of(carroDTO().numeroPortas(10), "carro cujo numero de portas é maior que 9")
        );
    }

    private VeiculoNaoEncontradoException veiculoNaoEncontradoException() {
        return new VeiculoNaoEncontradoException(VEICULO_ID);
    }
}