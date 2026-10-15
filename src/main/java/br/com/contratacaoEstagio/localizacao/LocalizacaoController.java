package br.com.contratacaoEstagio.localizacao;

import br.com.contratacaoEstagio.empresa.EmpresaRepository;
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
@RequestMapping("/localizacao")
public class LocalizacaoController {
    private final LocalizacaoService localizacaoService;

    public LocalizacaoController(LocalizacaoService localizacaoService, EmpresaRepository empresaRepository){
        this.localizacaoService = localizacaoService;
    }

    @PostMapping
    public ResponseEntity<Localizacao> cadastrar(@RequestBody Localizacao localizacao){
        Localizacao localSalvo = localizacaoService.cadastrar(localizacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(localSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Localizacao> atualizar(@PathVariable("id") Long id, @RequestBody Localizacao localizacao){
        Localizacao localAtualizado = localizacaoService.atualizar(id, localizacao);
        return ResponseEntity.ok(localAtualizado);
    }

    @GetMapping 
    public ResponseEntity<List<Localizacao>> ListarTodos(){
        return ResponseEntity.ok(localizacaoService.ListarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Long id){
        if(!localizacaoService.verificarId(id)){
            return ResponseEntity.notFound().build();
        }

        localizacaoService.excluirLocalizacao(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<Localizacao> buscarCep(@PathVariable String cep){
        return ResponseEntity.ok(localizacaoService.buscarCep(cep));
    }

}
