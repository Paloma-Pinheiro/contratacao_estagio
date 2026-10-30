package br.com.contratacaoEstagio.contrato;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.contratacaoEstagio.aluno.AlunoService;
import br.com.contratacaoEstagio.empresa.EmpresaService;

@Controller
public class ContratoPageController {

    private final ContratoService contratoService;
    private final AlunoService alunoService;
    private final EmpresaService empresaService;

    public ContratoPageController(
            ContratoService contratoService,
            AlunoService alunoService,
            EmpresaService empresaService) {

        this.contratoService = contratoService;
        this.alunoService = alunoService;
        this.empresaService = empresaService;
    }

    @GetMapping("/contratos")
    public String contrato(Model model) {

        model.addAttribute("listarContratos", contratoService.listarTodos());
        model.addAttribute("listarAlunos", alunoService.listarTodos());
        model.addAttribute("listarEmpresas", empresaService.listarTodos());

        return "contrato/contrato";
    }
}