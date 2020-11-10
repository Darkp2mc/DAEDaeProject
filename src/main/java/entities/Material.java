/*package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
        name = "MATERIAIS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"NOME"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllMateriais",
                query = "SELECT s FROM Material s ORDER BY s.nome" // JPQL
        )
})
public class Material {

    //carateristicas do material dependendo da variante

    @Id
    private String nome;
    @NotNull
    private String tipo;
    @NotNull
    private String familia;
    @NotNull
    private String dimensoes;

    @ManyToMany
    @JoinTable(name = "MATERIAIS_ESTRUTURAS",
            joinColumns = @JoinColumn(name = "MATERIAL_NOME", referencedColumnName = "NOME"),
            inverseJoinColumns = @JoinColumn(name = "ESTRUTURA_NOME", referencedColumnName = "NOME"))
    private List<Estrutura> estruturas;

    public Material() {
    }

    public Material(String nome, @NotNull String tipo, @NotNull String familia, @NotNull String dimensoes) {
        this.nome = nome;
        this.tipo = tipo;
        this.familia = familia;
        this.dimensoes = dimensoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public void addEstrutura(Estrutura estrutura){
        this.estruturas.add(estrutura);
    }

    public void removeEstrutura(Estrutura estrutura){
        this.estruturas.remove(estrutura);
    }
}
*/