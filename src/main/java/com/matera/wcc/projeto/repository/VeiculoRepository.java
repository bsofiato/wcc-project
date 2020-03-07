package com.matera.wcc.projeto.repository;

import com.matera.wcc.projeto.domain.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @todo <i>extra</i> colocar busca paginada
 */
@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {
}
