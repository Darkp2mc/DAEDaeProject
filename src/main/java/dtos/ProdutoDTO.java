package dtos;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ProdutoDTO implements Serializable {

    private String nome;
    private String tipo;
    private String familia;
    private String dimensoes;
    private List<VarianteDTO> varianteDTOs;
    private List<EstruturaDTO> estruturaDTOS;
    private String fabricanteNome;

    public ProdutoDTO() {
        this.estruturaDTOS = new LinkedList<>();
    }

    public ProdutoDTO(String nome, String tipo, String familia, String fabricanteNome) {
        this.nome = nome;
        this.fabricanteNome = fabricanteNome;
        this.tipo = tipo;
        this.familia = familia;
        this.varianteDTOs = new LinkedList<>();
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

    public String getFabricanteNome() {
        return fabricanteNome;
    }

    public void setFabricanteNome(String fabricanteNome) {
        this.fabricanteNome = fabricanteNome;
    }
}
