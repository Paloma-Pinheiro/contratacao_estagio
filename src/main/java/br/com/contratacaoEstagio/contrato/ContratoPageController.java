package br.com.contratacaoEstagio.contrato;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContratoPageController {
    public final ContratoService contratoService;
    
    public ContratoPageController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }
    
    @GetMapping("/contratos")
    public String contrato(Model model) {
        model.addAttribute("listarContratos", contratoService.ListarTodos());
        return "contrato/contrato";
    }
    
}
