package dtos;

import java.io.Serializable;
import java.util.List;

public class EstruturaDTO implements Serializable {

    private String nome;
    private String tipoDeProduto;
    private String projetoNome;
    private List<ProdutoDTO> produtoDTOS;

    //TODO dimensoes


    public EstruturaDTO() {
    }

    public EstruturaDTO(String nome, String tipoDeProduto, String projetoNome) {
        this.nome = nome;
        this.tipoDeProduto = tipoDeProduto;
        this.projetoNome = projetoNome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoDeProduto() {
        return tipoDeProduto;
    }

    public void setTipoDeProduto(String tipoDeProduto) {
        this.tipoDeProduto = tipoDeProduto;
    }

    public String getProjetoNome() {
        return projetoNome;
    }

    public void setProjetoNome(String projetoNome) {
        this.projetoNome = projetoNome;
    }

    public List<ProdutoDTO> getProdutoDTOS() {
        return produtoDTOS;
    }

    public void setProdutoDTOS(List<ProdutoDTO> produtoDTOS) {
        this.produtoDTOS = produtoDTOS;
    }

    public void addProdutoDTO(ProdutoDTO produtoDTO){
        this.produtoDTOS.add(produtoDTO);
    }

    public void removeProdutoDTO(ProdutoDTO produtoDTO){
        this.produtoDTOS.remove(produtoDTO);
    }
}
