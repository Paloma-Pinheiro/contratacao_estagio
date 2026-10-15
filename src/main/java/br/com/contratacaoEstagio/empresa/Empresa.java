package br.com.contratacaoEstagio.empresa;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name="empresa")
@Getter 
@Setter 
@NoArgsConstructor 
public class Empresa {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String razaoSocial;
    private String segmento;
    private String areaAtuacao;
    private boolean ativo = true;
}
