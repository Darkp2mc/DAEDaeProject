package dtos;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class VarianteDTO implements Serializable {

    private int codigo;
    private String produtoNome;
    private String nome;

    private double weff_p;
    private double weff_n;
    private double ar;
    private double sigmaC;
    private double pp;

    private LinkedHashMap<Double,Double> mcr_p;

    private LinkedHashMap<Double,Double> mcr_n;

    public VarianteDTO() {
        this.mcr_p = new LinkedHashMap<Double,Double>();
        this.mcr_n = new LinkedHashMap<Double,Double>();
    }

    public VarianteDTO(int codigo, String produtoNome, String nome, double weff_p, double weff_n, double ar, double sigmaC, double pp) {
        this.codigo = codigo;
        this.produtoNome = produtoNome;
        this.nome = nome;
        this.weff_p = weff_p;
        this.weff_n = weff_n;
        this.ar = ar;
        this.sigmaC = sigmaC;
        this.pp = pp;
        this.mcr_p = new LinkedHashMap<Double,Double>();
        this.mcr_n = new LinkedHashMap<Double,Double>();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int code) {
        this.codigo = code;
    }

    public String getProdutoNome() {
        return this.produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        this.nome = name;
    }

    public double getWeff_p() {
        return weff_p;
    }

    public void setWeff_p(double weff_p) {
        this.weff_p = weff_p;
    }

    public double getWeff_n() {
        return weff_n;
    }

    public void setWeff_n(double weff_n) {
        this.weff_n = weff_n;
    }

    public double getAr() {
        return ar;
    }

    public void setAr(double ar) {
        this.ar = ar;
    }

    public double getSigmaC() {
        return sigmaC;
    }

    public void setSigmaC(double sigmaC) {
        this.sigmaC = sigmaC;
    }

    public double getPp() {
        return pp;
    }

    public void setPp(double pp) {
        this.pp = pp;
    }

    public LinkedHashMap<Double, Double> getMcr_p() {
        return mcr_p;
    }

    public void setMcr_p(LinkedHashMap<Double, Double> mcr_p) {
        this.mcr_p = mcr_p;
    }

    public void addMcr_p(Double L, Double mcr_pValue){
        mcr_p.put(L, mcr_pValue);
    }

    public void removeMcr_p(Double LToRemove){
        mcr_p.remove(LToRemove);
    }

    public LinkedHashMap<Double, Double> getMcr_n() {
        return mcr_n;
    }

    public void setMcr_n(LinkedHashMap<Double, Double> mcr_n) {
        this.mcr_n = mcr_n;
    }

    public void addMcr_n(Double L, Double mcr_nValue){
        mcr_n.put(L, mcr_nValue);
    }

    public void removeMcr_n(Double LToRemove){
        mcr_n.remove(LToRemove);
    }
}
