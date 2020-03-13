package com.matera.wcc.projeto.rest;

import com.matera.wcc.projeto.domain.Caminhao;
import com.matera.wcc.projeto.domain.Carro;
import com.matera.wcc.projeto.domain.Moto;
import com.matera.wcc.projeto.domain.Veiculo;
import com.matera.wcc.projeto.rest.dto.CaminhaoDTO;
import com.matera.wcc.projeto.rest.dto.CarroDTO;
import com.matera.wcc.projeto.rest.dto.MotoDTO;
import com.matera.wcc.projeto.rest.dto.VeiculoDTO;
import com.matera.wcc.projeto.service.VeiculoNaoEncontradoException;
import com.matera.wcc.projeto.service.VeiculoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class VeiculosApiDelegateImpl implements VeiculosApiDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(VeiculosApiDelegateImpl.class);

    private final VeiculoService veiculoService;
    private final ModelMapper modelMapper;

    public VeiculosApiDelegateImpl(VeiculoService veiculoService, ModelMapper modelMapper) {
        this.veiculoService = veiculoService;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<List<VeiculoDTO>> getVeiculos(Integer page, Integer size) throws Exception {
        LOGGER.debug("Preparando busca de veiculos [page: {}, size: {1}]");
        Pageable pageable = PageRequest.of(page, size);
        Page<VeiculoDTO> veiculos = this.veiculoService.findAll(pageable).map(this::convert);
        LOGGER.info("Busca de veiculos realizada com sucesso [{} de {} veiculos retornados]", veiculos.getContent().size(), veiculos.getTotalElements());
        return ResponseEntity.ok()
                             .header("X-Total-Count", String.valueOf(veiculos.getTotalElements()))
                             .body(veiculos.getContent());
    }

    @Override
    public ResponseEntity<VeiculoDTO> getVeiculo(UUID veiculoId) throws Exception {
        LOGGER.debug("Preparando busca por veiculo de id {}", veiculoId);
        VeiculoDTO veiculoDTO = convert(this.veiculoService.findById(veiculoId));
        LOGGER.info("Busca de veiculo de id {} retornou {}", veiculoId, veiculoDTO);
        return ResponseEntity.ok(veiculoDTO);
    }

    @Override
    public ResponseEntity<Void> createVeiculo(VeiculoDTO veiculoDTO) throws Exception {
        LOGGER.debug("Inserindo novo veiculo [dados: {}]", veiculoDTO);
        UUID id = this.veiculoService.insert(convert(veiculoDTO));
        LOGGER.info("Novo veiculo inserido com sucesso [id: {}]", id);
        return ResponseEntity.created(location(id)).build();
    }

    @Override
    public ResponseEntity<VeiculoDTO> updateVeiculo(UUID veiculoId, VeiculoDTO veiculoDTO) throws Exception {
        LOGGER.debug("Atualizando veiculo de id {} [dados: {}]", veiculoId, veiculoDTO);
        VeiculoDTO veiculoDTOWithId = veiculoDTO.id(veiculoId);
        this.veiculoService.update(convert(veiculoDTOWithId));
        LOGGER.info("Veiculo de id {} atualizado com sucesso [dados: {}]", veiculoId, veiculoDTO);
        return ResponseEntity.ok(veiculoDTOWithId);
    }

    @Override
    public ResponseEntity<Void> deleteVeiculo(UUID veiculoId) throws Exception {
        LOGGER.debug("Deletando veiculo de id {}", veiculoId);
        this.veiculoService.delete(veiculoId);
        LOGGER.info("Veiculo de id {} deletado com sucesso", veiculoId);
        return ResponseEntity.noContent().build();
    }

    @PostConstruct
    public void configureModelMapper() {
        modelMapper.createTypeMap(Carro.class, VeiculoDTO.class).setConverter(ctx -> modelMapper.map(ctx.getSource(), CarroDTO.class));
        modelMapper.createTypeMap(Moto.class, VeiculoDTO.class).setConverter(ctx -> modelMapper.map(ctx.getSource(), MotoDTO.class));
        modelMapper.createTypeMap(Caminhao.class, VeiculoDTO.class).setConverter(ctx -> modelMapper.map(ctx.getSource(), CaminhaoDTO.class));

        modelMapper.createTypeMap(CarroDTO.class, Veiculo.class).setConverter(ctx -> modelMapper.map(ctx.getSource(), Carro.class));
        modelMapper.createTypeMap(MotoDTO.class, Veiculo.class).setConverter(ctx -> modelMapper.map(ctx.getSource(), Moto.class));
        modelMapper.createTypeMap(CaminhaoDTO.class, Veiculo.class).setConverter(ctx -> modelMapper.map(ctx.getSource(), Caminhao.class));
    }

    private URI location(UUID id) {
        URI location = UriComponentsBuilder.fromPath("/v1/veiculos/{id}").build(id.toString());
        LOGGER.trace("Criando header de localizacao de novo veiculo [Location: {}]", location.toString());
        return location;
    }

    private VeiculoDTO convert(Veiculo veiculo) {
        return this.modelMapper.map(veiculo, VeiculoDTO.class);
    }
    private Veiculo convert(VeiculoDTO veiculo) {
        return this.modelMapper.map(veiculo, Veiculo.class);
    }
}
