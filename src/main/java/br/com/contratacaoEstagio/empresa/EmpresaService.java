package br.com.contratacaoEstagio.empresa;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.contratacaoEstagio.contrato.ContratoRepository;

@Service 
public class EmpresaService {
    private final EmpresaRepository empresaRepository;
    private final ContratoRepository contratoRepository;

    public EmpresaService(
            EmpresaRepository empresaRepository,
            ContratoRepository contratoRepository) {
        this.empresaRepository = empresaRepository;
        this.contratoRepository = contratoRepository;
    }

    public Empresa cadastrar(Empresa empresa){
        return empresaRepository.save(empresa);
    }
    public void alternarStatus(Long id) {
        Empresa empresa = buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada"));
        empresa.setAtivo(!empresa.isAtivo());
        empresaRepository.save(empresa);
    }

    public Empresa atualizar(Long id, Empresa dadosAtualizados){
        Empresa empresa = empresaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada."));

        empresa.setAreaAtuacao(dadosAtualizados.getAreaAtuacao());
        empresa.setRazaoSocial(dadosAtualizados.getRazaoSocial());
        empresa.setSegmento(dadosAtualizados.getSegmento());

        return empresaRepository.save(empresa);
    }

    public List<Empresa> listarTodos(){
        return empresaRepository.findAll();
    }

    public Optional<Empresa> buscarPorId(Long id){
        return empresaRepository.findById(id);
    }

    public boolean verificarId(Long id){
        return empresaRepository.existsById(id);
    }

    public void excluirEmpresa(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Empresa não encontrada.");
        }

        if (contratoRepository.existsByEmpresa_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir a empresa porque existem contratos vinculados.");
        }

        empresaRepository.deleteById(id);
    }
}
