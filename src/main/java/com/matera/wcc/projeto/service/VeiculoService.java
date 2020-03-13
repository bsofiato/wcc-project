package com.matera.wcc.projeto.service;

import com.matera.wcc.projeto.domain.Veiculo;
import com.matera.wcc.projeto.repository.VeiculoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class VeiculoService {
    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional(readOnly = true)
    public Veiculo findById(UUID id) throws VeiculoNaoEncontradoException {
        return this.veiculoRepository.findById(id).orElseThrow(() -> new VeiculoNaoEncontradoException(id));
    }

    @Transactional(readOnly = true)
    public Page<Veiculo> findAll(Pageable page) {
        return this.veiculoRepository.findAll(page);
    }

    @Transactional
    public UUID insert(Veiculo veiculo) {
        return this.veiculoRepository.save(veiculo).getId();
    }

    @Transactional
    public void delete(UUID uuid) throws VeiculoNaoEncontradoException {
        ifExists(uuid, () -> this.veiculoRepository.deleteById(uuid));
    }

    @Transactional
    public void update(Veiculo veiculo) throws VeiculoNaoEncontradoException {
        ifExists(veiculo.getId(), () -> {
            this.veiculoRepository.save(veiculo);
        });
    }

    private void ifExists(UUID uuid, Runnable exec) throws VeiculoNaoEncontradoException {
        if (this.findById(uuid) != null) {
            exec.run();
        }
    }
}
