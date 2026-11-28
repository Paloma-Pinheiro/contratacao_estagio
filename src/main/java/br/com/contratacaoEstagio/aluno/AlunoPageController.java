
package br.com.contratacaoEstagio.aluno;

import br.com.contratacaoEstagio.localizacao.Localizacao;
import br.com.contratacaoEstagio.localizacao.LocalizacaoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AlunoPageController {

    private final AlunoService alunoService;
    private final LocalizacaoService localizacaoService;

    public AlunoPageController(
            AlunoService alunoService,
            LocalizacaoService localizacaoService) {
        this.alunoService = alunoService;
        this.localizacaoService = localizacaoService;
    }

    @GetMapping("/aluno")
    public String aluno(Model model) {
        model.addAttribute("listarUsuarios", alunoService.listarTodos());
        model.addAttribute("modoEdicao", false);
        model.addAttribute("formAction", "/aluno");
        model.addAttribute("tituloFormulario", "Cadastro do Aluno");
        model.addAttribute("textoBotao", "Finalizar cadastro");
        return "aluno/aluno";
    }

    @GetMapping("/aluno/{ra}/editar")
    public String editarFormulario(
            @PathVariable String ra,
            Model model) {

        Aluno aluno = alunoService.buscarPorRa(ra)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aluno não encontrado: " + ra));

        var localizacoes = localizacaoService.buscarPorAlunoRa(ra);

        if (localizacoes.size() != 1) {
            throw new IllegalStateException(
                    "O aluno deve possuir exatamente uma localização.");
        }

        model.addAttribute("listarUsuarios", alunoService.listarTodos());
        model.addAttribute("modoEdicao", true);
        model.addAttribute("formAction", "/aluno/" + ra + "/editar");
        model.addAttribute("tituloFormulario", "Editar Aluno");
        model.addAttribute("textoBotao", "Salvar alterações");
        model.addAttribute("alunoEdicao", aluno);
        model.addAttribute("localizacaoEdicao", localizacoes.get(0));

        return "aluno/aluno";
    }

    @PostMapping("/aluno")
    public String cadastrar(
            @ModelAttribute Aluno aluno,
            @ModelAttribute Localizacao localizacao,
            RedirectAttributes redirectAttributes) {

        if (!alunoService.cadastrarComLocalizacao(aluno, localizacao)) {
            redirectAttributes.addFlashAttribute(
                    "erro", "Já existe um aluno cadastrado com esse RA.");
            return "redirect:/aluno";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso", "Aluno cadastrado com sucesso.");

        return "redirect:/aluno";
    }

    @PostMapping("/aluno/{ra}/editar")
    public String atualizar(
            @PathVariable String ra,
            @ModelAttribute Aluno aluno,
            @ModelAttribute Localizacao localizacao,
            RedirectAttributes redirectAttributes) {

        boolean atualizado = alunoService.atualizarComLocalizacao(
                ra, aluno, localizacao);

        if (!atualizado) {
            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não foi possível atualizar: o aluno deve possuir exatamente uma localização.");
            return "redirect:/aluno";
        }

        redirectAttributes.addFlashAttribute(
                "sucesso", "Aluno atualizado com sucesso.");

        return "redirect:/aluno";
    }

    @PostMapping("/aluno/{ra}/ativar")
    public String alternarAtivo(
            @PathVariable String ra,
            RedirectAttributes redirectAttributes) {

        alunoService.Ativar(ra);

        redirectAttributes.addFlashAttribute(
                "sucesso", "Status do aluno atualizado.");

        return "redirect:/aluno";
    }

    @PostMapping("/aluno/{ra}/excluir")
    public String excluir(
            @PathVariable String ra,
            RedirectAttributes redirectAttributes) {

        alunoService.excluirPorRa(ra);

        redirectAttributes.addFlashAttribute(
                "sucesso", "Aluno excluído com sucesso.");

        return "redirect:/aluno";
    }
}
