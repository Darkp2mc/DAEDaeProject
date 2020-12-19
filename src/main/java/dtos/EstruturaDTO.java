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

    private double numeroDeVaos;//Chapa, Lage, Painel, Perfil
    private double comprimentoDaVao;//Chapa, Lage, Painel, Perfil
    private String aplicacao;//Chapa, Painel, Perfil
    private double alturaDaLage;//Lage
    private double sobrecarga;//Chapa, Lage, Painel, Perfil

    private int estado;

    public EstruturaDTO() {
        this.varianteDTOs = new LinkedList<>();
    }

    public EstruturaDTO(String nome, String tipoDeProduto, String projetoNome,
                        double numeroDeVaos, double comprimentoDaVao, String aplicacao,
                        double alturaDaLage, double sobrecarga, int estado) {
        this.nome = nome;
        this.tipoDeProduto = tipoDeProduto;
        this.projetoNome = projetoNome;
        this.numeroDeVaos = numeroDeVaos;
        this.comprimentoDaVao = comprimentoDaVao;
        this.aplicacao = aplicacao;
        this.alturaDaLage = alturaDaLage;
        this.sobrecarga = sobrecarga;
        this.varianteDTOs = new LinkedList<>();
        this.estado = estado;
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

    public double getNumeroDeVaos() {
        return numeroDeVaos;
    }

    public void setNumeroDeVaos(double numeroDeVaos) {
        this.numeroDeVaos = numeroDeVaos;
    }

    public double getComprimentoDaVao() {
        return comprimentoDaVao;
    }

    public void setComprimentoDaVao(double comprimentoDaVao) {
        this.comprimentoDaVao = comprimentoDaVao;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public double getAlturaDaLage() {
        return alturaDaLage;
    }

    public void setAlturaDaLage(double alturaDaLage) {
        this.alturaDaLage = alturaDaLage;
    }

    public double getSobrecarga() {
        return sobrecarga;
    }

    public void setSobrecarga(double sobrecarga) {
        this.sobrecarga = sobrecarga;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void aceitar() {
        this.estado = 1;
    }
    public void rejeitar() {
        this.estado = -1;
    }
}
