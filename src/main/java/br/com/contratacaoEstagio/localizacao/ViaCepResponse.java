package br.com.contratacaoEstagio.localizacao;

public record ViaCepResponse(
    String cep,
    String logradouro,
    String bairro,
    String localidade,
    boolean erro
) {}
