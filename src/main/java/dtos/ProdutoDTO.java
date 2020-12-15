package dtos;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ProdutoDTO implements Serializable {

    private String nome;
    private String tipo;
    private String familia;
    private double e;
    private double n;
    private double g;
    private List<VarianteDTO> varianteDTOs;
    private List<EstruturaDTO> estruturaDTOS;
    private String fabricanteNome;

    public ProdutoDTO() {
        this.estruturaDTOS = new LinkedList<>();
    }

    public ProdutoDTO(String nome, String tipo, String familia, double e, double n, double g, String fabricanteNome) {
        this.nome = nome;
        this.fabricanteNome = fabricanteNome;
        this.tipo = tipo;
        this.familia = familia;
        this.e = e;
        this.n = n;
        this.g = g;
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

    public String getFabricanteNome() {
        return fabricanteNome;
    }

    public void setFabricanteNome(String fabricanteNome) {
        this.fabricanteNome = fabricanteNome;
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
        this.g = g;
    }
}
