package com.matera.wcc.projeto.service;

import com.matera.wcc.projeto.domain.Veiculo;
import com.matera.wcc.projeto.repository.VeiculoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class VeiculoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VeiculoService.class);

    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional(readOnly = true)
    public Veiculo findById(UUID id) throws VeiculoNaoEncontradoException {
        LOGGER.debug("Obtendo veiculo de id {}", id);
        Veiculo veiculo = this.veiculoRepository.findById(id).orElseThrow(() -> {
            LOGGER.warn("Veiculo de id {} nao encontrado", id);
            return new VeiculoNaoEncontradoException(id);
        });
        LOGGER.info("Veiculo de id {} obtido com sucesso [veiculo: {}]", id, veiculo);
        return veiculo;
    }

    @Transactional(readOnly = true)
    public Page<Veiculo> findAll(Pageable page) {
        LOGGER.debug("Obtendo veiculos [page: {}]", page);
        Page<Veiculo> result = this.veiculoRepository.findAll(page);
        LOGGER.info("Obtido {} veiculos (de {}) [page: {}]", result.getContent().size(), result.getTotalElements(), page);
        return result;
    }

    @Transactional
    public UUID insert(Veiculo veiculo) {
        LOGGER.debug("Preparando para salvar veiculo {}", veiculo);
        UUID id = this.veiculoRepository.save(veiculo).getId();
        LOGGER.info("Veiculo de id {} salvo com sucesso [dados: {}]", id, veiculo);
        return id;
    }

    @Transactional
    public void delete(UUID uuid) throws VeiculoNaoEncontradoException {
        LOGGER.debug("Deletando veiculo de id {}", uuid);
        ifExists(uuid, () -> {
            this.veiculoRepository.deleteById(uuid);
            LOGGER.info("Veiculo de id {} deletado com sucesso");
        });
    }

    @Transactional
    public void update(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        LOGGER.debug("Atualizando veiculo de id {} [dados: {}]", veiculo.getId(), veiculo);
        ifExists(veiculo.getId(), () -> {
            this.veiculoRepository.save(veiculo);
            LOGGER.info("Veiculo de id {} atualizado com sucesso [dados: {}]", veiculo.getId(), veiculo);
        });
    }

    private void ifExists(UUID uuid, Runnable exec) throws VeiculoNaoEncontradoException {
        LOGGER.trace("Verificando se o veiculo de id {} existe");
        if (this.veiculoRepository.findById(uuid).isPresent()) {
            exec.run();
        } else {
            LOGGER.warn("Veiculo de id {} nao encontrado", uuid);
            throw new VeiculoNaoEncontradoException(uuid);
        }
    }
}
