package entities;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public class Estrutura {
    //parametros de calculo

    @Id
    private String nome;
    @NotNull
    private Material material;

    private LinkedList<Material> materiais;
    @NotNull
    private String dimensoes;

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

    public LinkedList<Material> getMateriais() {
        return materiais;
    }

    public void setMateriais(LinkedList<Material> materiais) {
        this.materiais = materiais;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }
}
