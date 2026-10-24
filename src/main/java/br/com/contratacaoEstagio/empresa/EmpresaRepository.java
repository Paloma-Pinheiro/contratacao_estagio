package br.com.contratacaoEstagio.empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{    
    Long countByativoTrue();
}
