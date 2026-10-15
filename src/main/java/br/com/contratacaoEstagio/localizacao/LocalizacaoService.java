package br.com.contratacaoEstagio.localizacao;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LocalizacaoService {
    private final LocalizacaoRepository localizacaoRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public LocalizacaoService(LocalizacaoRepository localizacaoRepository) {
        this.localizacaoRepository = localizacaoRepository;
    }

    public Localizacao cadastrar(Localizacao localizacao) {
        return localizacaoRepository.save(localizacao);
    }

    public Localizacao atualizar(Long id, Localizacao dadosAtualizados) {
        Localizacao localizacao = localizacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Localizacao não encontrada"));

        localizacao.setAluno(dadosAtualizados.getAluno());
        localizacao.setBairro(dadosAtualizados.getBairro());
        localizacao.setCep(dadosAtualizados.getCep());
        localizacao.setCidade(dadosAtualizados.getCidade());
        localizacao.setComplemento(dadosAtualizados.getComplemento());
        localizacao.setEndereco(dadosAtualizados.getEndereco());
        localizacao.setNumero(dadosAtualizados.getNumero());

        return localizacaoRepository.save(localizacao);
    }

    public List<Localizacao> ListarTodos() {
        return localizacaoRepository.findAll();
    }

    public Optional<Localizacao> buscarPorId(Long id) {
        return localizacaoRepository.findById(id);
    }

    public boolean verificarId(Long id) {
        return localizacaoRepository.existsById(id);
    }

    public void excluirLocalizacao(Long id) {
        localizacaoRepository.deleteById(id);
        ;
    }

    public Localizacao buscarCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        ViaCepResponse resposta = restTemplate.getForObject(url, ViaCepResponse.class);

        if (resposta == null || Boolean.TRUE.equals(resposta.erro())) {
            throw new RuntimeException("CEP não encontrado.");
        }

        Localizacao localizacao = new Localizacao();
        localizacao.setCep(resposta.cep());
        localizacao.setEndereco(resposta.logradouro());
        localizacao.setBairro(resposta.bairro());
        localizacao.setCidade(resposta.localidade());

        return localizacao;
    }

}
