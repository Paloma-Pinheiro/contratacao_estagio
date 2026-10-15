package br.com.contratacaoEstagio.contrato;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, Long>{
    boolean existsByEmpresa_Id(Long empresaId);
} 
