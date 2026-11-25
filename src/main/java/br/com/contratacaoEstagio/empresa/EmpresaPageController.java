package br.com.contratacaoEstagio.empresa;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmpresaPageController {

    private final EmpresaService empresaService;

    public EmpresaPageController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping("/empresa")
    public String abrirTelaEmpresa(Model model) {
        prepararFormulario(model, new Empresa(), false);
        return "empresa/empresa";
    }

    @GetMapping("/empresa/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        Empresa empresa = empresaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empresa não encontrada."));

        prepararFormulario(model, empresa, true);
        return "empresa/empresa";
    }

    @PostMapping("/empresa")
    public String cadastrar(
            @ModelAttribute Empresa empresa,
            RedirectAttributes redirectAttributes) {

        empresaService.cadastrar(empresa);
        redirectAttributes.addFlashAttribute(
                "sucesso", "Empresa cadastrada com sucesso.");
        return "redirect:/empresa";
    }

    @PostMapping("/empresa/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Empresa empresa,
            RedirectAttributes redirectAttributes) {

        empresaService.atualizar(id, empresa);
        redirectAttributes.addFlashAttribute(
                "sucesso", "Empresa atualizada com sucesso.");
        return "redirect:/empresa";
    }

    @PostMapping("/empresa/{id}/ativar")
    public String alternarStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        empresaService.alternarStatus(id);
        redirectAttributes.addFlashAttribute(
                "sucesso", "Status da empresa atualizado.");
        return "redirect:/empresa";
    }

    @PostMapping("/empresa/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            empresaService.excluirEmpresa(id);
            redirectAttributes.addFlashAttribute(
                    "sucesso", "Empresa excluída com sucesso.");
        } catch (ResponseStatusException exception) {
            String mensagem = exception.getReason() != null
                    ? exception.getReason()
                    : "Não foi possível excluir a empresa.";
            redirectAttributes.addFlashAttribute("erro", mensagem);
        }

        return "redirect:/empresa";
    }

    private void prepararFormulario(
            Model model, Empresa empresa, boolean modoEdicao) {
        model.addAttribute("listarEmpresas", empresaService.listarTodos());
        model.addAttribute("empresaForm", empresa);
        model.addAttribute("modoEdicao", modoEdicao);
        model.addAttribute(
                "formAction",
                modoEdicao ? "/empresa/" + empresa.getId() + "/editar" : "/empresa");
    }
}