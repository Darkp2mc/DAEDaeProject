package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
        name = "ESTRUTURAS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"NOME"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllEstruturas",
                query = "SELECT s FROM Estrutura s ORDER BY s.nome" // JPQL
        )
})
public class Estrutura {
    //parametros de calculo

    @Id
    private String nome;

    @ManyToMany
    @JoinTable(name = "ESTRUTURAS_PRODUTOS",
            joinColumns = @JoinColumn(name = "ESTRUTURA_NOME", referencedColumnName = "NOME"),
            inverseJoinColumns = @JoinColumn(name = "PRODUTO_NOME", referencedColumnName = "NOME"))
    @NotNull
    private List<Produto> produtos;

    @NotNull
    private String dimensoes;

    @ManyToOne
    @JoinColumn(name = "PROJETO_NOME")
    @NotNull
    private Projeto projeto;

    @NotNull
    private String tipoDeProduto;

    public Estrutura() {

    }

    public Estrutura(String nome, @NotNull String tipoDeProduto, @NotNull String dimensoes) {
        this.nome = nome;
        this.tipoDeProduto = tipoDeProduto;
        this.dimensoes = dimensoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void addProduto(Produto produto){
        this.produtos.add(produto);
    }
    public void removeProduto(Produto produto){
        this.produtos.remove(produto);
    }

    public String getTipoDeProduto() {
        return tipoDeProduto;
    }

    public void setTipoDeProduto(String tipoDeProduto) {
        this.tipoDeProduto = tipoDeProduto;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
