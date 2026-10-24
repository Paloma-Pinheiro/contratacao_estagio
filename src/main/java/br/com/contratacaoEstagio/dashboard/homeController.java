package br.com.contratacaoEstagio.dashboard;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.contratacaoEstagio.aluno.AlunoRepository;
import br.com.contratacaoEstagio.contrato.ContratoRepository;
import br.com.contratacaoEstagio.empresa.EmpresaRepository;

@Controller
public class homeController {
    private static final int DIAS_ALERTA = 30;
    private final AlunoRepository alunoRepository;
    private final EmpresaRepository empresaRepository;
    private final ContratoRepository contratoRepository;

    

    public homeController(AlunoRepository alunoRepository, EmpresaRepository empresaRepository,
            ContratoRepository contratoRepository) {
        this.alunoRepository = alunoRepository;
        this.empresaRepository = empresaRepository;
        this.contratoRepository = contratoRepository;
    }



    @GetMapping("/")
    public String dashboard(Model model) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(DIAS_ALERTA);

        model.addAttribute("totalAlunos", alunoRepository.count());
        model.addAttribute("alunosAtivos", alunoRepository.countByativoTrue());
        model.addAttribute("totalEmpresa", empresaRepository.count());
        model.addAttribute("empresasAtivas", empresaRepository.countByativoTrue());
        model.addAttribute("contratosAtivos", contratoRepository.countByativoTrue());
        model.addAttribute("contratosVencendo", contratoRepository.countByAtivoTrueAndDataTerminoBetween(hoje, limite));
        return "dashboard";
    }
}
