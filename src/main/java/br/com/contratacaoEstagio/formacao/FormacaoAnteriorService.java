package br.com.contratacaoEstagio.formacao;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service 
public class FormacaoAnteriorService {
    private final FormacaoAnteriorRepository formacaoRepository;
    
    public FormacaoAnteriorService(FormacaoAnteriorRepository formacaoAnterior){
        this.formacaoRepository = formacaoAnterior;
    }

    public FormacaoAnterior cadastrar(FormacaoAnterior formacao){
        return formacaoRepository.save(formacao);
    }

    public FormacaoAnterior atualizar(Long id, FormacaoAnterior dadosAtualizados){
        FormacaoAnterior formacao = formacaoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Formacao não encontrada."));

        formacao.setCursoTecnico(dadosAtualizados.isCursoTecnico());
        formacao.setEscolaOrigem(dadosAtualizados.getEscolaOrigem());
        formacao.setEscolaridade(dadosAtualizados.getEscolaridade());
        formacao.setGraduacaoTI(dadosAtualizados.isGraduacaoTI());
        formacao.setPossuiGraduacao(dadosAtualizados.isPossuiGraduacao());
        formacao.setTecnicoTI(dadosAtualizados.isTecnicoTI());
        formacao.setTipoescola(dadosAtualizados.getTipoescola());

        return formacaoRepository.save(formacao);
    }

    public List<FormacaoAnterior> ListarTodos(){
        return formacaoRepository.findAll();
    }

    public Optional<FormacaoAnterior> buscarPorId(Long id){
        return formacaoRepository.findById(id);
    }

    public boolean verificarId(Long id){
        return formacaoRepository.existsById(id);
    }

    public void excluirFormacao(Long id){
        formacaoRepository.deleteById(id);
    }
}
