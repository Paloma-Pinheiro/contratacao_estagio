package br.com.contratacaoEstagio.aluno;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlunoPageController {

    public final AlunoService alunoService;

    public AlunoPageController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping("/aluno")
    public String aluno(Model model) {
        model.addAttribute("listarUsuarios", alunoService.listarTodos());
        return "aluno/aluno";
    }

}
