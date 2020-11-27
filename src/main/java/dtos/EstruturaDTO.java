package dtos;

import entities.Projeto;
import enums.AplicacaoPertendida;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class EstruturaDTO implements Serializable {

    private String nome;
    private String tipoDeProduto;
    private String projetoNome;
    private List<ProdutoDTO> produtoDTOS;

    private String numeroDeVaos;//Chapa, Lage, Painel, Perfil
    private String comprimentoDaVao;//Chapa, Lage, Painel, Perfil
    private String aplicacao;//Chapa, Painel, Perfil
    private String alturaDaLage;//Lage


    public EstruturaDTO() {
    }

    public EstruturaDTO(String nome, String tipoDeProduto, String projetoNome,
                        String numeroDeVaos, String comprimentoDaVao, String aplicacao,
                        String alturaDaLage) {
        this.nome = nome;
        this.tipoDeProduto = tipoDeProduto;
        this.projetoNome = projetoNome;
        this.numeroDeVaos = numeroDeVaos;
        this.comprimentoDaVao = comprimentoDaVao;
        this.aplicacao = aplicacao;
        this.alturaDaLage = alturaDaLage;
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

    public String getNumeroDeVaos() {
        return numeroDeVaos;
    }

    public void setNumeroDeVaos(String numeroDeVaos) {
        this.numeroDeVaos = numeroDeVaos;
    }

    public String getComprimentoDaVao() {
        return comprimentoDaVao;
    }

    public void setComprimentoDaVao(String comprimentoDaVao) {
        this.comprimentoDaVao = comprimentoDaVao;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public String getAlturaDaLage() {
        return alturaDaLage;
    }

    public void setAlturaDaLage(String alturaDaLage) {
        this.alturaDaLage = alturaDaLage;
    }
}
