package br.com.contratacaoEstagio.contrato;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    public Contrato cadastrar(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    public Contrato atualizar(Long id, Contrato dadosAtualizados) {

        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contrato não encontrado."
                ));

        contrato.setAluno(dadosAtualizados.getAluno());
        contrato.setArea(dadosAtualizados.getArea());
        contrato.setCargaHoraria(dadosAtualizados.getCargaHoraria());
        contrato.setCargo(dadosAtualizados.getCargo());
        contrato.setDataInicio(dadosAtualizados.getDataInicio());
        contrato.setDataTermino(dadosAtualizados.getDataTermino());
        contrato.setEmpresa(dadosAtualizados.getEmpresa());
        contrato.setHorario(dadosAtualizados.getHorario());
        contrato.setModalidade(dadosAtualizados.getModalidade());
        contrato.setAtivo(dadosAtualizados.isAtivo());
        return contratoRepository.save(contrato);
    }

    public List<Contrato> listarTodos() {
        return contratoRepository.findAll();
    }

    public Optional<Contrato> buscarPorId(Long id) {
        return contratoRepository.findById(id);
    }

    public boolean verificarId(Long id) {
        return contratoRepository.existsById(id);
    }

    @Transactional
    public void excluirContrato(Long id) {

        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contrato não encontrado."
                ));

        boolean empresaAtiva = contrato.getEmpresa().isAtivo();
        boolean alunoAtivo = contrato.getAluno().isAtivo();

        if (empresaAtiva && alunoAtivo) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O contrato só pode ser excluído se a empresa "
                            + "ou o aluno estiver inativo."
            );
        }

        contratoRepository.delete(contrato);
    }
}