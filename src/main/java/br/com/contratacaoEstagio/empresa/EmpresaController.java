package br.com.contratacaoEstagio.empresa;

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
@RequestMapping("/api/empresa")
public class EmpresaController {
    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService){
        this.empresaService = empresaService;
    }

    @PostMapping 
    public ResponseEntity<Empresa> cadastrar(@RequestBody Empresa empresa){

        Empresa empresaSalva = empresaService.cadastrar(empresa);

        return ResponseEntity.status(HttpStatus.CREATED).body(empresaSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> atualizar(@PathVariable("id") Long id, @RequestBody Empresa dadosAtualizados){
        Empresa empresaAtualizada = empresaService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(empresaAtualizada);        
    }

    @GetMapping 
    public ResponseEntity<List<Empresa>> listarTodos(){
        return ResponseEntity.ok(empresaService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(@PathVariable Long id){
        if(!empresaService.verificarId(id)){
            return ResponseEntity.notFound().build();
        }

        empresaService.excluirEmpresa(id);

        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/ativar")
        public ResponseEntity<Void> alternarStatus(@PathVariable Long id) {
            empresaService.alternarStatus(id);
            return ResponseEntity.ok().build();
        }
}
