package br.com.contratacaoEstagio.contrato;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, Long>{
    boolean existsByEmpresa_Id(Long empresaId);
    Long countByativoTrue();
    long countByAtivoTrueAndDataTerminoBetween(LocalDate inicio, LocalDate fim);
} 
