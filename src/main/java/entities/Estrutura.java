package entities;

import enums.AplicacaoPertendida;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    @JoinTable(name = "ESTRUTURAS_VARIANTES",
            joinColumns = @JoinColumn(name = "ESTRUTURA_NOME", referencedColumnName = "NOME"),
            inverseJoinColumns = @JoinColumn(name = "VARIANTE_NOME", referencedColumnName = "NOME"))

    private List<Variante> variantes;

    @ManyToOne
    @JoinColumn(name = "PROJETO_NOME")
    @NotNull
    private Projeto projeto;

    @NotNull
    private String tipoDeProduto;


    private double numeroDeVaos;//Chapa, Lage, Painel, Perfil
    private double comprimentoDaVao;//Chapa, Lage, Painel, Perfil
    private String aplicacao;//Chapa, Painel, Perfil
    private double alturaDaLage;//Lage
    private double sobrecarga;//Chapa, Lage, Painel, Perfil

    @NotNull
    private int estado;

    public Estrutura() {
        variantes = new LinkedList<>();
    }

    public Estrutura(String nome, @NotNull Projeto projeto, @NotNull String tipoDeProduto,
                     double numeroDeVaos, double comprimentoDaVao, String aplicacao,
                     double alturaDaLage, double sobrecarga) {
        this.nome = nome;
        this.projeto = projeto;
        this.tipoDeProduto = tipoDeProduto;
        this.numeroDeVaos = numeroDeVaos;
        this.comprimentoDaVao = comprimentoDaVao;
        this.aplicacao = aplicacao;
        this.alturaDaLage = alturaDaLage;
        this.sobrecarga = sobrecarga;
        this.estado = 0;
        variantes = new LinkedList<>();
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

    public List<Variante> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<Variante> variantes) {
        this.variantes = variantes;
    }
    public void addVariante(Variante variante){
        this.variantes.add(variante);
    }

    public void removeVariante(Variante variante){
        this.variantes.remove(variante);
    }

    public String getTipoDeProduto() {
        return tipoDeProduto;
    }

    public void setTipoDeProduto(String tipoDeProduto) {
        this.tipoDeProduto = tipoDeProduto;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public double getNumeroDeVaos() {
        return numeroDeVaos;
    }

    public void setNumeroDeVaos(double numeroDeVaos) {
        this.numeroDeVaos = numeroDeVaos;
    }

    public double getComprimentoDaVao() {
        return comprimentoDaVao;
    }

    public void setComprimentoDaVao(double comprimentoDaVao) {
        this.comprimentoDaVao = comprimentoDaVao;
    }

    public double getAlturaDaLage() {
        return alturaDaLage;
    }

    public void setAlturaDaLage(double alturaDaLage) {
        this.alturaDaLage = alturaDaLage;
    }

    public double getSobrecarga() {
        return sobrecarga;
    }

    public void setSobrecarga(double sobrecarga) {
        this.sobrecarga = sobrecarga;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado){
        this.estado= estado;
    }

    public void aceitar() {
        this.estado = 1;
    }
    public void rejeitar() {
        this.estado = -1;
    }

    private void setDimencoesPeloTipo(){
        if (getTipoDeProduto().equals("Chapa") ||
                getTipoDeProduto().equals("Painel") ||
                getTipoDeProduto().equals("Perfil")){
            this.alturaDaLage = 0;
        }
        else if(getTipoDeProduto().equals("Lage")){
            this.aplicacao = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estrutura estrutura = (Estrutura) o;
        return nome.equals(estrutura.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}
