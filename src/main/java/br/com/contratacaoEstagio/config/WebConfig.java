
package br.com.contratacaoEstagio.config;

import br.com.contratacaoEstagio.interceptador.ApiKeyInterceptador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiKeyInterceptador apiKeyInterceptador;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(apiKeyInterceptador)
    .addPathPatterns(
        "/api/aluno/**",
        "/api/empresa/**",
        "/contrato/**",
        "/formacao/**",
        "/localizacao/**"
    )
    .excludePathPatterns("/**/public/**");
}
}
