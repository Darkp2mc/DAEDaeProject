package dtos;

import entities.Projeto;
import enums.AplicacaoPertendida;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class EstruturaDTO implements Serializable {

    private String nome;
    private String tipoDeProduto;
    private String projetoNome;
    private List<VarianteDTO> varianteDTOs;

    private String numeroDeVaos;//Chapa, Lage, Painel, Perfil
    private String comprimentoDaVao;//Chapa, Lage, Painel, Perfil
    private String aplicacao;//Chapa, Painel, Perfil
    private String alturaDaLage;//Lage
    private String sobrecarga;//Chapa, Lage, Painel, Perfil

    private int estado;

    public EstruturaDTO() {
        this.varianteDTOs = new LinkedList<>();
    }

    public EstruturaDTO(String nome, String tipoDeProduto, String projetoNome,
                        String numeroDeVaos, String comprimentoDaVao, String aplicacao,
                        String alturaDaLage, String sobrecarga) {
        this.nome = nome;
        this.tipoDeProduto = tipoDeProduto;
        this.projetoNome = projetoNome;
        this.numeroDeVaos = numeroDeVaos;
        this.comprimentoDaVao = comprimentoDaVao;
        this.aplicacao = aplicacao;
        this.alturaDaLage = alturaDaLage;
        this.sobrecarga = sobrecarga;
        this.varianteDTOs = new LinkedList<>();
        this.estado = 0;
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

    public List<VarianteDTO> getVarianteDTOs() {
        return varianteDTOs;
    }

    public void setVarianteDTOs(List<VarianteDTO> varianteDTOS) {
        this.varianteDTOs = varianteDTOS;
    }

    public void addVarianteDTO(VarianteDTO varianteDTO){
        this.varianteDTOs.add(varianteDTO);
    }

    public void removeVarianteDTO(VarianteDTO varianteDTO){
        this.varianteDTOs.remove(varianteDTO);
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

    public String getSobrecarga() {
        return sobrecarga;
    }

    public void setSobrecarga(String sobrecarga) {
        this.sobrecarga = sobrecarga;
    }

    public int getEstado() {
        return estado;
    }

    public void aceitar() {
        this.estado = 1;
    }
    public void rejeitar() {
        this.estado = -1;
    }
}
