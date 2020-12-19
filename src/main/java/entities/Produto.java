package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    @Version
    private int version;

    @Id
    private String nome;
    @NotNull
    private String tipo;
    @NotNull
    private String familia;

    @NotNull
    private double e;

    @NotNull
    private double n;

    private double g;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.REMOVE)
    private List<Variante> variantes;

    @NotNull
    @ManyToOne
    private Fabricante fabricante;

    public Produto() {
        variantes = new LinkedList<>();
    }

    public Produto(String nome, @NotNull String tipo, @NotNull String familia, @NotNull double e, @NotNull double n, @NotNull Fabricante fabricante) {
        this.nome = nome;
        this.fabricante = fabricante;
        this.tipo = tipo;
        this.familia = familia;
        this.e = e;
        this.n = n;
        this.g = round(e,n);
        variantes = new LinkedList<>();
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

    public double getE() {
        return e;
    }

    public void setE(double e) {
        this.e = e;
    }

    public double getN() {
        return n;
    }

    public void setN(double n) {
        this.n = n;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = round(e,n);
    }

    public static double round(double e,double n) {
        double scale = Math.pow(10, 2);
        return Math.round((e/(2*(1+n))) * scale) / scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(nome, produto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }
}