package br.com.contratacaoEstagio.formacao;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "FormacaoAnterior")
@Getter 
@Setter
@NoArgsConstructor 
public class FormacaoAnterior {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String escolaridade;
    private String escolaOrigem;
    
    @Enumerated (EnumType.STRING)
    private TipoEscola tipoescola;
    
    private boolean cursoTecnico;
    private boolean tecnicoTI;
    private boolean possuiGraduacao;
    private boolean graduacaoTI;
}
