package br.com.contratacaoEstagio.contrato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import br.com.contratacaoEstagio.aluno.Aluno;
import br.com.contratacaoEstagio.aluno.AlunoService;
import br.com.contratacaoEstagio.empresa.Empresa;
import br.com.contratacaoEstagio.empresa.EmpresaService;

@ExtendWith(MockitoExtension.class)
class ContratoPageControllerTest {

    @Mock
    private ContratoService contratoService;

    @Mock
    private AlunoService alunoService;

    @Mock
    private EmpresaService empresaService;

    @Mock
    private Model model;

    @InjectMocks
    private ContratoPageController controller;

    @Test
    void deveCarregarContratosAlunosEmpresasERetornarView() {
        List<Contrato> contratos = List.of();
        List<Aluno> alunos = List.of();
        List<Empresa> empresas = List.of();

        when(contratoService.listarTodos()).thenReturn(contratos);
        when(alunoService.listarTodos()).thenReturn(alunos);
        when(empresaService.listarTodos()).thenReturn(empresas);

        String view = controller.contrato(model);

        assertEquals("contrato/contrato", view);
        verify(model).addAttribute("listarContratos", contratos);
        verify(model).addAttribute("listarAlunos", alunos);
        verify(model).addAttribute("listarEmpresas", empresas);
    }
}
