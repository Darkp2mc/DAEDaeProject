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

    @ManyToOne
    @JoinColumn(name = "MATERIAL_NOME")
    @NotNull
    private Material material;

    @ManyToMany(mappedBy = "estruturas")
    private List<Material> materiais;

    @NotNull
    private String dimensoes;

    @ManyToOne @JoinColumn(name = "FABRICANTE_NOME")
    private Fabricante fabricante;

    public Estrutura() {
        this.materiais = new LinkedList<>();
    }

    public Estrutura(String nome, @NotNull Material material, @NotNull String dimensoes) {
        this.nome = nome;
        this.material = material;
        this.dimensoes = dimensoes;
        this.materiais = new LinkedList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public List<Material> getMateriais() {
        return materiais;
    }

    public void setMateriais(List<Material> materiais) {
        this.materiais = materiais;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }

    public Fabricante getFabricante() {
        return fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public void addMaterial(Material material){
        this.materiais.add(material);
    }

    public void removeMaterial(Material material){
        this.materiais.remove(material);
    }
}
