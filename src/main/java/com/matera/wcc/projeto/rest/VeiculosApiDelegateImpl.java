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
    private final VeiculoService veiculoService;
    private final ModelMapper modelMapper;

    public VeiculosApiDelegateImpl(VeiculoService veiculoService, ModelMapper modelMapper) {
        this.veiculoService = veiculoService;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<List<VeiculoDTO>> getVeiculos(Integer page, Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<VeiculoDTO> veiculos = this.veiculoService.findAll(pageable).map(this::convert);
        return ResponseEntity.ok()
                             .header("X-Total-Count", String.valueOf(veiculos.getTotalElements()))
                             .body(veiculos.getContent());
    }

    @Override
    public ResponseEntity<VeiculoDTO> getVeiculo(UUID veiculoId) throws Exception {
        return ResponseEntity.ok(convert(this.veiculoService.findById(veiculoId)));
    }

    @Override
    public ResponseEntity<Void> createVeiculo(VeiculoDTO veiculoDTO) throws Exception {
        UUID id = this.veiculoService.insert(convert(veiculoDTO));
        return ResponseEntity.created(location(id)).build();
    }

    @Override
    public ResponseEntity<VeiculoDTO> updateVeiculo(UUID veiculoId, VeiculoDTO veiculoDTO) throws Exception {
        VeiculoDTO veiculoDTOWithId = veiculoDTO.id(veiculoId);
        this.veiculoService.update(convert(veiculoDTOWithId));
        return ResponseEntity.ok(veiculoDTOWithId);
    }

    @Override
    public ResponseEntity<Void> deleteVeiculo(UUID veiculoId) throws Exception {
        this.veiculoService.delete(veiculoId);
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
        return UriComponentsBuilder.fromPath("/v1/veiculos/{id}").build(id.toString());
    }

    private VeiculoDTO convert(Veiculo veiculo) {
        return this.modelMapper.map(veiculo, VeiculoDTO.class);
    }
    private Veiculo convert(VeiculoDTO veiculo) {
        return this.modelMapper.map(veiculo, Veiculo.class);
    }
}
