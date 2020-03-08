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
    public Optional<Veiculo> findById(UUID id) {
        return this.veiculoRepository.findById(id);
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
    public boolean delete(UUID uuid) {
        Optional<Veiculo> veiculo = this.veiculoRepository.findById(uuid);
        if (veiculo.isPresent()) {
            this.veiculoRepository.delete(veiculo.get());
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean update(Veiculo veiculo) {
        Optional<Veiculo> loadedVeiculo = this.veiculoRepository.findById(veiculo.getId());
        if (loadedVeiculo.isPresent()) {
            this.veiculoRepository.save(veiculo);
            return true;
        } else {
            return false;
        }
    }
}
