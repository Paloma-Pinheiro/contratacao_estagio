package br.com.contratacaoEstagio.aluno;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("api/aluno")
public class AlunoController {
    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public ResponseEntity<Aluno> cadastrar(@RequestBody Aluno aluno) {
        if (alunoService.verificarRa(aluno.getRa())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Aluno alunoSalvo = alunoService.cadastrar(aluno);

        return ResponseEntity.status(HttpStatus.CREATED).body(alunoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable("id") String ra, @RequestBody Aluno dadosAtualizados) {
        Aluno aluno = alunoService.atualizar(ra, dadosAtualizados);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/{ra}/desativar")
    public RedirectView desativarAluno(@PathVariable("ra") String ra) {
        alunoService.desativar(ra);

        return new RedirectView("/aluno", true);
    }

    @PostMapping("/{ra}/ativar")
    public RedirectView alternarAtivo(@PathVariable("ra") String ra) {
        alunoService.Ativar(ra);

        return new RedirectView("/aluno", true);
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> ListarTodos(Model model) {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @DeleteMapping("/{ra}/deletar")
    public ResponseEntity<Void> excluirPorRa(@PathVariable String ra) {
        if (!alunoService.verificarRa(ra)) {
            return ResponseEntity.notFound().build();
        }

        alunoService.excluirPorRa(ra);

        return ResponseEntity.noContent().build();
    }

}
