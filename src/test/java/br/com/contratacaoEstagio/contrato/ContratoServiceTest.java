package br.com.contratacaoEstagio.contrato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.com.contratacaoEstagio.aluno.Aluno;
import br.com.contratacaoEstagio.aluno.AlunoRepository;
import br.com.contratacaoEstagio.empresa.Empresa;
import br.com.contratacaoEstagio.empresa.EmpresaRepository;

@ExtendWith(MockitoExtension.class)
class ContratoServiceTest {

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private ContratoService contratoService;

    @Test
    void deveAssociarAlunoEEmpresaPersistidosAoCadastrar() {
        Aluno alunoInformado = new Aluno();
        alunoInformado.setRa("RA123");
        Empresa empresaInformada = new Empresa();
        empresaInformada.setId(7L);
        Contrato contrato = new Contrato();
        contrato.setAluno(alunoInformado);
        contrato.setEmpresa(empresaInformada);

        Aluno alunoPersistido = new Aluno();
        alunoPersistido.setRa("RA123");
        Empresa empresaPersistida = new Empresa();
        empresaPersistida.setId(7L);
        when(alunoRepository.findById("RA123"))
                .thenReturn(Optional.of(alunoPersistido));
        when(empresaRepository.findById(7L))
                .thenReturn(Optional.of(empresaPersistida));
        when(contratoRepository.save(contrato)).thenReturn(contrato);

        Contrato salvo = contratoService.cadastrar(contrato);

        assertSame(contrato, salvo);
        assertSame(alunoPersistido, salvo.getAluno());
        assertSame(empresaPersistida, salvo.getEmpresa());
        verify(contratoRepository).save(contrato);
    }

    @Test
    void naoDeveSalvarContratoComAlunoInexistente() {
        Aluno alunoInformado = new Aluno();
        alunoInformado.setRa("RA-inexistente");
        Empresa empresaInformada = new Empresa();
        empresaInformada.setId(7L);
        Contrato contrato = new Contrato();
        contrato.setAluno(alunoInformado);
        contrato.setEmpresa(empresaInformada);
        when(alunoRepository.findById("RA-inexistente"))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> contratoService.cadastrar(contrato));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(contratoRepository, never()).save(contrato);
    }

    @Test
    void deveAtualizarCamposEAssociacoesSemAlterarIdDoContrato() {
        Contrato existente = new Contrato();
        existente.setId(3L);
        Aluno alunoInformado = new Aluno();
        alunoInformado.setRa("RA123");
        Empresa empresaInformada = new Empresa();
        empresaInformada.setId(7L);
        Contrato dadosAtualizados = new Contrato();
        dadosAtualizados.setAluno(alunoInformado);
        dadosAtualizados.setEmpresa(empresaInformada);
        dadosAtualizados.setArea("Tecnologia");

        Aluno alunoPersistido = new Aluno();
        alunoPersistido.setRa("RA123");
        Empresa empresaPersistida = new Empresa();
        empresaPersistida.setId(7L);
        when(contratoRepository.findById(3L))
                .thenReturn(Optional.of(existente));
        when(alunoRepository.findById("RA123"))
                .thenReturn(Optional.of(alunoPersistido));
        when(empresaRepository.findById(7L))
                .thenReturn(Optional.of(empresaPersistida));
        when(contratoRepository.save(existente)).thenReturn(existente);

        Contrato atualizado = contratoService.atualizar(3L, dadosAtualizados);

        assertEquals(3L, atualizado.getId());
        assertEquals("Tecnologia", atualizado.getArea());
        assertSame(alunoPersistido, atualizado.getAluno());
        assertSame(empresaPersistida, atualizado.getEmpresa());
    }

    @Test
    void naoDeveExcluirContratoQuandoAlunoEEmpresaEstaoAtivos() {
        Contrato contrato = new Contrato();
        Aluno aluno = new Aluno();
        aluno.setAtivo(true);
        Empresa empresa = new Empresa();
        empresa.setAtivo(true);
        contrato.setAluno(aluno);
        contrato.setEmpresa(empresa);
        when(contratoRepository.findById(3L))
                .thenReturn(Optional.of(contrato));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> contratoService.excluirContrato(3L));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(contratoRepository, never()).delete(contrato);
    }
}
