package entities;

import enums.AplicacaoPertendida;

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

    private List<Produto> produtos;

    @ManyToOne
    @JoinColumn(name = "PROJETO_NOME")
    @NotNull
    private Projeto projeto;

    @NotNull
    private String tipoDeProduto;


    private String numeroDeVaos;//Chapa, Lage, Painel, Perfil
    private String comprimentoDaVao;//Chapa, Lage, Painel, Perfil
    private String aplicacao;//Chapa, Painel, Perfil
    private String alturaDaLage;//Lage

    public Estrutura() {

    }

    public Estrutura(String nome, @NotNull Projeto projeto, @NotNull String tipoDeProduto,
                     String numeroDeVaos, String comprimentoDaVao, String aplicacao,
                     String alturaDaLage) {
        this.nome = nome;
        this.projeto = projeto;
        this.tipoDeProduto = tipoDeProduto;
        this.numeroDeVaos = numeroDeVaos;
        this.comprimentoDaVao = comprimentoDaVao;
        this.aplicacao = aplicacao;
        this.alturaDaLage = alturaDaLage;

        setDimencoesPeloTipo();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public String getNumeroDeVaos() {
        return numeroDeVaos;
    }

    public void setNumeroDeVaos(String numeroDeVaos) {
        this.numeroDeVaos = numeroDeVaos;
    }

    public String getComprimentoDaVao() {
        return comprimentoDaVao;
    }

    public void setComprimentoDaVao(String comprimentoDaVao) {
        this.comprimentoDaVao = comprimentoDaVao;
    }

    public String getAlturaDaLage() {
        return alturaDaLage;
    }

    public void setAlturaDaLage(String alturaDaLage) {
        this.alturaDaLage = alturaDaLage;
    }

    private void setDimencoesPeloTipo(){
        if (getTipoDeProduto().equals("Chapa") ||
                getTipoDeProduto().equals("Painel") ||
                getTipoDeProduto().equals("Perfil")){
            this.alturaDaLage = null;
        }
        else if(getTipoDeProduto().equals("Lage")){
            this.aplicacao = null;
        }
    }
}
