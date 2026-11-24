package br.com.contratacaoEstagio.localizacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {

    List<Localizacao> findByAluno_Ra(String ra);

    void deleteByAluno_Ra(String ra);

}
