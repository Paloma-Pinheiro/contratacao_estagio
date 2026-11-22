package br.com.contratacaoEstagio.interceptador;

import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptador implements HandlerInterceptor {

    // Diferente da professora, que utiliza uma lista de chaves
    // (da ultima aula) utilizamos uma variável de ambiente para não deixar
    // a chave exposta no código ou no application.properties
    // comentado por hora, vamos deixar igual o da professora
    //@Value("${app.security.api-key}")
    // private String apiKeyValida;

    private final Set<String> CHAVES_VALIDAS = Set.of("chave123");

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        System.out.println("Verificando acesso à API: " + path);

        // Verifica se a requisição possui a API Key
        String apiKey = request.getHeader("X-API-KEY");

        // só serve se for variavel de ambiente, mas ta comentado por hora, vamos deixar igual o da professora
        // se voltar a utilizar a variavel de ambiente, descomentar a linha 22 e comentar a linha 26
        //if (apiKey == null || !apiKey.equals(apiKeyValida)) {

        if (apiKey == null || !isValidApiKey(apiKey)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(
                "{\"erro\":\"Acesso não autorizado. API Key inválida ou ausente.\"}"
            );

            return false;
        }

        return true;
    }
    private boolean isValidApiKey(String apiKey) {
        if (!CHAVES_VALIDAS.contains(apiKey)) {
            return false;
        }

        return true;
    }
}
