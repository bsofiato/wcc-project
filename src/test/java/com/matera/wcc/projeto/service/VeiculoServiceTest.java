package com.matera.wcc.projeto.service;

import com.matera.wcc.projeto.domain.Carro;
import com.matera.wcc.projeto.domain.Veiculo;
import com.matera.wcc.projeto.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VeiculoServiceTest {
    private static final UUID VEICULO_ID = UUID.randomUUID();
    private static final Veiculo VEICULO = new Carro();
    private static final Page<Veiculo> PAGE = new PageImpl<>(Arrays.asList(VEICULO));

    @Mock
    private VeiculoRepository repository;

    private VeiculoService service;

    @BeforeEach
    public void setupService() {
        this.service = new VeiculoService(repository);
    }

    @Test
    public void notFoundById() {
        doReturn(Optional.empty()).when(repository).findById(VEICULO_ID);

        assertThat(service.findById(VEICULO_ID)).isEmpty();

        verify(repository, times(1)).findById(VEICULO_ID);
    }

    @Test
    public void foundById() {
        doReturn(Optional.of(VEICULO)).when(repository).findById(VEICULO_ID);

        assertThat(service.findById(VEICULO_ID)).containsSame(VEICULO);

        verify(repository, times(1)).findById(VEICULO_ID);
    }

    @Test
    public void findAll() {
        doReturn(PAGE).when(repository).findAll(PageRequest.of(0, 10));

        assertThat(service.findAll(PageRequest.of(0, 10))).isSameAs(PAGE);

        verify(repository, times(1)).findAll(PageRequest.of(0, 10));
    }

}