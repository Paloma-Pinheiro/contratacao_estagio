package br.com.contratacaoEstagio.contrato;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.contratacaoEstagio.aluno.Aluno;
import br.com.contratacaoEstagio.aluno.AlunoRepository;
import br.com.contratacaoEstagio.empresa.Empresa;
import br.com.contratacaoEstagio.empresa.EmpresaRepository;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final AlunoRepository alunoRepository;
    private final EmpresaRepository empresaRepository;

    public ContratoService(
            ContratoRepository contratoRepository,
            AlunoRepository alunoRepository,
            EmpresaRepository empresaRepository) {
        this.contratoRepository = contratoRepository;
        this.alunoRepository = alunoRepository;
        this.empresaRepository = empresaRepository;
    }

    public Contrato cadastrar(Contrato contrato) {
        contrato.setAluno(buscarAluno(contrato.getAluno()));
        contrato.setEmpresa(buscarEmpresa(contrato.getEmpresa()));
        return contratoRepository.save(contrato);
    }

    public Contrato atualizar(Long id, Contrato dadosAtualizados) {

        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contrato não encontrado."
                ));

        Aluno aluno = buscarAluno(dadosAtualizados.getAluno());
        Empresa empresa = buscarEmpresa(dadosAtualizados.getEmpresa());

        contrato.setAluno(aluno);
        contrato.setArea(dadosAtualizados.getArea());
        contrato.setCargaHoraria(dadosAtualizados.getCargaHoraria());
        contrato.setCargo(dadosAtualizados.getCargo());
        contrato.setDataInicio(dadosAtualizados.getDataInicio());
        contrato.setDataTermino(dadosAtualizados.getDataTermino());
        contrato.setEmpresa(empresa);
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

    private Aluno buscarAluno(Aluno alunoInformado) {
        if (alunoInformado == null
                || alunoInformado.getRa() == null
                || alunoInformado.getRa().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Aluno não encontrado.");
        }

        return alunoRepository.findById(alunoInformado.getRa())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado."));
    }

    private Empresa buscarEmpresa(Empresa empresaInformada) {
        if (empresaInformada == null || empresaInformada.getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Empresa não encontrada.");
        }

        return empresaRepository.findById(empresaInformada.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empresa não encontrada."));
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