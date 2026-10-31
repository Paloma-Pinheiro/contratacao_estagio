package br.com.contratacaoEstagio.empresa;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpresaPageController {

    private final EmpresaService empresaService;

    public EmpresaPageController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping("/empresa")
    public String abrirTelaEmpresa(Model model) {
        model.addAttribute("listarEmpresas", empresaService.listarTodos());
        return "empresa/empresa";
    }
}