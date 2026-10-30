package br.com.contratacaoEstagio.aluno;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alunos")
@Getter 
@Setter  
@NoArgsConstructor 
public class Aluno{
    @Id 
    private String ra;
    private String nome;
    private String curso;
    private int anoIngresso;
    private int semestreAtual;
    private int qtTentativasVestibular;
    private String rendaFamiliar;
    private String faixaEtaria;
    private String etnia;
    private String genero;
    private String modalidadeIngresso;
    private boolean pcd;
    private boolean ativo = true;
    
    public void addAtribute(String string, long count) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAtribute'");
    }
}
