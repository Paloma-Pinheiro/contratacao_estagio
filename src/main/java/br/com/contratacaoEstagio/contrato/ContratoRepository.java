package br.com.contratacaoEstagio.contrato;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Procedure;
import org.springframework.data.jpa.repository.Query;

public interface ContratoRepository extends JpaRepository<Contrato, Long>{
    boolean existsByEmpresa_Id(Long empresaId);
    Long countByativoTrue();
    long countByAtivoTrueAndDataTerminoBetween(LocalDate inicio, LocalDate fim);

    // Chamada a Stored Procedure (modifica dados): ver sql/procedures.sql
    @Procedure(procedureName = "encerrar_contratos_vencidos", outputParameterName = "total")
    Integer encerrarContratosVencidos();

    // Relatorio: total de contratos e contratos ativos por empresa
    @Query("SELECT c.empresa.id, COUNT(c), SUM(CASE WHEN c.ativo = true THEN 1 ELSE 0 END) "
         + "FROM Contrato c GROUP BY c.empresa.id ORDER BY c.empresa.id")
    List<Object[]> relatorioPorEmpresa();
}
