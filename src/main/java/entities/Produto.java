package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
        name = "PRODUTO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"NOME"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllProdutos",
                query = "SELECT s FROM Produto s ORDER BY s.nome" // JPQL
        )
})
public class Produto {

    @Id
    private String nome;
    private String tipo;
    private String familia;
    private String dimensoes;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.REMOVE)
    private List<Variante> variantes;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.REMOVE)
    private List<Estrutura> estruturas;

    @NotNull
    @ManyToOne
    private Fabricante fabricante;

    public Produto() {
        variantes = new LinkedList<>();
        estruturas = new LinkedList<>();
    }

    public Produto(String nome, Fabricante fabricante) {
        this.nome = nome;
        this.fabricante = fabricante;
        variantes = new LinkedList<>();
        estruturas = new LinkedList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        this.nome = name;
    }

    public List<Variante> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<Variante> especimen) {
        this.variantes = especimen;
    }

    public void addVariante(Variante s) {
        variantes.add(s);
    }

    public void removeVariante(Variante s) {
        variantes.remove(s);
    }

    public List<Estrutura> getEstruturas() {
        return estruturas;
    }

    public void setEstruturas(List<Estrutura> estruturas) {
        this.estruturas = estruturas;
    }

    public void addEstrutura(Estrutura estrutura){
        this.estruturas.add(estrutura);
    }

    public void removeEstrutura(Estrutura estrutura){
        this.estruturas.remove(estrutura);
    }

    public Fabricante getFabricante() {
        return fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }
}