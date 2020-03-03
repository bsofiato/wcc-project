package com.matera.wcc.projeto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.matera.wcc.projeto.repository") // Inicializa os repositorios do spring data
@EnableTransactionManagement // Inicializa as transacoes
public class RepositoryConfiguration {
}
