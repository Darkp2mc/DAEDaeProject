package dtos;

import java.io.Serializable;

public class EstruturaDTO implements Serializable {

    private String nome;
    private String produtoNome;
    private String projetoNome;
    //TODO dimensoes


    public EstruturaDTO() {
    }

    public EstruturaDTO(String nome, String produtoNome, String projetoNome) {
        this.nome = nome;
        this.produtoNome = produtoNome;
        this.projetoNome = projetoNome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public String getProjetoNome() {
        return projetoNome;
    }

    public void setProjetoNome(String projetoNome) {
        this.projetoNome = projetoNome;
    }
}
