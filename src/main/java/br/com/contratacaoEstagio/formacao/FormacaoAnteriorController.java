package br.com.contratacaoEstagio.formacao;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/formacao")
public class FormacaoAnteriorController {
    private final FormacaoAnteriorService formacaoService;

    public FormacaoAnteriorController(FormacaoAnteriorService formacaoService){
        this.formacaoService = formacaoService;
    }

    @PostMapping 
    public ResponseEntity<FormacaoAnterior> cadastrar(@RequestBody FormacaoAnterior formacao){
        FormacaoAnterior formacaoSalva = formacaoService.cadastrar(formacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(formacaoSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormacaoAnterior> atualizar(@PathVariable("id") Long id, @RequestBody FormacaoAnterior dadosAtualizados){
        FormacaoAnterior formacaoAtualizada = formacaoService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(formacaoAtualizada);
    }

    @GetMapping
    public ResponseEntity<List<FormacaoAnterior>> ListarTodos(){
        return ResponseEntity.ok(formacaoService.ListarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Long id){
        if(!formacaoService.verificarId(id)){
            return ResponseEntity.notFound().build();
        }

        formacaoService.excluirFormacao(id);

        return ResponseEntity.noContent().build();
    }
}
