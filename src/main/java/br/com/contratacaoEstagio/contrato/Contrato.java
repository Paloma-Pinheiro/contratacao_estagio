package br.com.contratacaoEstagio.contrato;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import br.com.contratacaoEstagio.aluno.Aluno;
import br.com.contratacaoEstagio.empresa.Empresa;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "contrato_estagio")
@Getter 
@Setter
@NoArgsConstructor 
public class Contrato {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String area;
    private String cargo;
    private String horario;
    private Integer cargaHoraria;

    @Enumerated (EnumType.STRING)
    private Modalidade modalidade;
    
    private LocalDate dataInicio;
    private LocalDate dataTermino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_ra", nullable = false)
    private Aluno aluno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    private boolean ativo;
    public long calcularDuracao(){
        if(dataInicio == null || dataTermino == null){
            throw new IllegalStateException("É necessário informas as datas de início e termino.");
        }

        if(dataTermino.isBefore(dataInicio)){
            throw new IllegalStateException("A data de término não pode ser anterior a data de início.");
        }

        return ChronoUnit.DAYS.between(dataInicio, dataTermino);
    }
}
