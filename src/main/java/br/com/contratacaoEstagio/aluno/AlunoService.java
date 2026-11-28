
package br.com.contratacaoEstagio.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.contratacaoEstagio.localizacao.Localizacao;
import br.com.contratacaoEstagio.localizacao.LocalizacaoRepository;
import br.com.contratacaoEstagio.localizacao.LocalizacaoService;
import jakarta.transaction.Transactional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final LocalizacaoRepository localizacaoRepository;
    private final LocalizacaoService localizacaoService;

    public AlunoService(
            AlunoRepository alunoRepository,
            LocalizacaoRepository localizacaoRepository,
            LocalizacaoService localizacaoService) {
        this.alunoRepository = alunoRepository;
        this.localizacaoRepository = localizacaoRepository;
        this.localizacaoService = localizacaoService;
    }

    public Aluno cadastrar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    @Transactional
    public boolean cadastrarComLocalizacao(Aluno aluno, Localizacao localizacao) {
        if (verificarRa(aluno.getRa())) {
            return false;
        }

        Aluno alunoSalvo = alunoRepository.save(aluno);
        localizacao.setAluno(alunoSalvo);
        localizacaoService.cadastrar(localizacao);

        return true;
    }

    @Transactional
    public boolean atualizarComLocalizacao(
            String ra,
            Aluno dadosAtualizados,
            Localizacao dadosLocalizacao) {

        List<Localizacao> localizacoes = localizacaoService.buscarPorAlunoRa(ra);

        if (localizacoes.size() != 1) {
            return false;
        }

        Aluno alunoAtualizado = atualizar(ra, dadosAtualizados);
        dadosLocalizacao.setAluno(alunoAtualizado);
        localizacaoService.atualizar(
                localizacoes.get(0).getId(), dadosLocalizacao);

        return true;
    }

    public Aluno atualizar(String ra, Aluno dadosAtualizados) {
        Aluno aluno = alunoRepository.findById(ra)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado."));

        aluno.setAnoIngresso(dadosAtualizados.getAnoIngresso());
        aluno.setNome(dadosAtualizados.getNome());
        aluno.setAtivo(dadosAtualizados.isAtivo());
        aluno.setCurso(dadosAtualizados.getCurso());
        aluno.setEtnia(dadosAtualizados.getEtnia());
        aluno.setFaixaEtaria(dadosAtualizados.getFaixaEtaria());
        aluno.setGenero(dadosAtualizados.getGenero());
        aluno.setModalidadeIngresso(dadosAtualizados.getModalidadeIngresso());
        aluno.setPcd(dadosAtualizados.isPcd());
        aluno.setQtTentativasVestibular(dadosAtualizados.getQtTentativasVestibular());
        aluno.setRendaFamiliar(dadosAtualizados.getRendaFamiliar());
        aluno.setSemestreAtual(dadosAtualizados.getSemestreAtual());

        return alunoRepository.save(aluno);
    }

    @Transactional
    public void desativar(String ra) {
        Aluno aluno = alunoRepository.findById(ra)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado: " + ra));

        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }

    @Transactional
    public void Ativar(String ra) {
        Aluno aluno = alunoRepository.findById(ra)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado: " + ra));

        aluno.setAtivo(!aluno.isAtivo());
        alunoRepository.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorRa(String ra) {
        return alunoRepository.findById(ra);
    }

    public boolean verificarRa(String ra) {
        return alunoRepository.existsById(ra);
    }

    @Transactional
    public void excluirPorRa(String ra) {
        if (!alunoRepository.existsById(ra)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Aluno não encontrado.");
        }

        localizacaoRepository.deleteByAluno_Ra(ra);
        alunoRepository.deleteById(ra);
    }
}
