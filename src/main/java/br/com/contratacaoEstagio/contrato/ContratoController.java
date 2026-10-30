package br.com.contratacaoEstagio.contrato;

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
@RequestMapping("/contrato")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping
    public ResponseEntity<Contrato> cadastrar(
            @RequestBody Contrato contrato) {

        Contrato contratoSalvo = contratoService.cadastrar(contrato);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contratoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrato> atualizar( @PathVariable("id") Long id, @RequestBody Contrato dadosAtualizados) {
        Contrato contratoAtualizado = contratoService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(contratoAtualizado);
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> listarTodos() {
        return ResponseEntity.ok(contratoService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPorId(
            @PathVariable("id") Long id) {

        contratoService.excluirContrato(id);

        return ResponseEntity.noContent().build();
    }
}