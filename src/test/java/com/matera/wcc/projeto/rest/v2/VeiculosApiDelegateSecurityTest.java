package com.matera.wcc.projeto.rest.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matera.wcc.projeto.config.ErrorHandlingConfiguration;
import com.matera.wcc.projeto.config.ModelMapperConfiguration;
import com.matera.wcc.projeto.config.ResourceServerConfiguration;
import com.matera.wcc.projeto.domain.Carro;
import com.matera.wcc.projeto.persona.LoggedAsDavi;
import com.matera.wcc.projeto.rest.v2.dto.CarroDTO;
import com.matera.wcc.projeto.rest.v2.dto.CombustivelDTO;
import com.matera.wcc.projeto.rest.v2.dto.VeiculoDTO;
import com.matera.wcc.projeto.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.zalando.problem.spring.web.autoconfigure.security.SecurityConfiguration;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest()
@Import({
        ErrorHandlingConfiguration.class,
        ModelMapperConfiguration.class,
        ResourceServerConfiguration.class,
        V2VeiculosApiDelegateImpl.class,
        SecurityConfiguration.class,
        com.matera.wcc.projeto.config.SecurityConfiguration.class
})
public class VeiculosApiDelegateSecurityTest {

    private static final UUID VEICULO_ID = UUID.fromString("fe5c20ff-c863-407a-891d-e9fef61d3114");

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VeiculoService service;

    @Test
    public void usuarioNaoLogadoNaoPodeSalvarVeiculos() throws Exception {
        mockMvc.perform(createRequest(carroDTO()))
                .andExpect(status().isForbidden());

        verify(service, never()).insert(isA(Carro.class));
    }

    @Test
    @LoggedAsDavi
    public void operadoresNaoPodemSalvarVeiculos() throws Exception {
        mockMvc.perform(createRequest(carroDTO()))
                .andExpect(status().isForbidden());

        verify(service, never()).insert(isA(Carro.class));
    }

    @Test
    public void usuarioNaoLogadoNaoPodeVisualizarVeiculos() throws Exception {
        mockMvc.perform(findAllRequest(0, 10))
                .andExpect(status().isForbidden());

        verify(service, never()).findAll(isA(Pageable.class));
    }


    @Test
    public void usuarioNaoLogadoNaoPodeAtualizarVeiculos() throws Exception {
        mockMvc.perform(updateRequest(carroDTO()))
                .andExpect(status().isForbidden());

        verify(service, never()).update(isA(Carro.class));
    }

    @Test
    @LoggedAsDavi
    public void operadoresNaoPodemAtualizarVeiculos() throws Exception {
        mockMvc.perform(updateRequest(carroDTO()))
                .andExpect(status().isForbidden());

        verify(service, never()).update(isA(Carro.class));
    }

    @Test
    public void usuarioNaoLogadoNaoPodeDeletarVeiculos() throws Exception {
        mockMvc.perform(deleteRequest())
                .andExpect(status().isForbidden());

        verify(service, never()).update(isA(Carro.class));
    }

    @Test
    @LoggedAsDavi
    public void operadoresNaoPodemDeletarVeiculos() throws Exception {
        mockMvc.perform(deleteRequest())
                .andExpect(status().isForbidden());

        verify(service, never()).update(isA(Carro.class));
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

    private static CarroDTO carroDTO() {
        CarroDTO carro = new CarroDTO();
        carro.setMarca("VOLKSWAGEN");
        carro.setModelo("GOL");
        carro.setAnoFabricacao(2018);
        carro.setAnoModelo(2019);
        carro.setCombustivel(CombustivelDTO.ALCOOL);
        carro.setNumeroPortas(5);
        carro.setImportado(true);
        return carro;
    }
}