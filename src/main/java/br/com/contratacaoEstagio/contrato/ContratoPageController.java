package br.com.contratacaoEstagio.contrato;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        prepararFormulario(model, new Contrato(), false);
        return "contrato/contrato";
    }

    @GetMapping("/contratos/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Contrato contrato = contratoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Contrato não encontrado."));

        prepararFormulario(model, contrato, true);
        return "contrato/contrato";
    }

    @PostMapping("/contratos")
    public String cadastrar(
            @ModelAttribute Contrato contrato,
            RedirectAttributes redirectAttributes) {
        try {
            contratoService.cadastrar(contrato);
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Contrato cadastrado com sucesso.");
        } catch (ResponseStatusException exception) {
            adicionarErro(redirectAttributes, exception);
        }

        return "redirect:/contratos";
    }

    @PostMapping("/contratos/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Contrato contrato,
            RedirectAttributes redirectAttributes) {
        try {
            contratoService.atualizar(id, contrato);
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Contrato atualizado com sucesso.");
        } catch (ResponseStatusException exception) {
            adicionarErro(redirectAttributes, exception);
        }

        return "redirect:/contratos";
    }

    @PostMapping("/contratos/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            contratoService.excluirContrato(id);
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Contrato excluído com sucesso.");
        } catch (ResponseStatusException exception) {
            adicionarErro(redirectAttributes, exception);
        }

        return "redirect:/contratos";
    }

    private void prepararFormulario(
            Model model, Contrato contrato, boolean modoEdicao) {
        model.addAttribute("listarContratos", contratoService.listarTodos());
        model.addAttribute("listarAlunos", alunoService.listarTodos());
        model.addAttribute("listarEmpresas", empresaService.listarTodos());
        model.addAttribute("contratoForm", contrato);
        model.addAttribute("modoEdicao", modoEdicao);
        model.addAttribute(
                "formAction",
                modoEdicao
                        ? "/contratos/" + contrato.getId() + "/editar"
                        : "/contratos");
    }

    private void adicionarErro(
            RedirectAttributes redirectAttributes,
            ResponseStatusException exception) {
        String mensagem = exception.getReason() != null
                ? exception.getReason()
                : "Não foi possível concluir a operação do contrato.";
        redirectAttributes.addFlashAttribute("erro", mensagem);
    }
}