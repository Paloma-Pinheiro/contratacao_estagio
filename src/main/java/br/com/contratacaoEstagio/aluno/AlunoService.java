package br.com.contratacaoEstagio.aluno;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service 
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository){
        this.alunoRepository = alunoRepository;
    }

    public Aluno cadastrar(Aluno aluno){
        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(String ra, Aluno dadosAtualizados){
        Aluno aluno = alunoRepository.findById(ra).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aluno não encotrado."));

        aluno.setAnoIngresso(dadosAtualizados.getAnoIngresso());
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

    public List<Aluno> listarTodos(){
        return alunoRepository.findAll();
    }

    public Optional<Aluno> buscarPorRa(String ra){
        return alunoRepository.findById(ra);
    }

    public boolean verificarRa(String ra){
        return alunoRepository.existsById(ra);
    } 

    public void excluirPorRa(String ra) {
        alunoRepository.deleteById(ra);
    }
}
