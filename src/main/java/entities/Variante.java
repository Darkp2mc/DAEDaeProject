package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
        name = "VARIANTES",
        uniqueConstraints = @UniqueConstraint(columnNames = {"NOME"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllVariantes",
                query = "SELECT s FROM Variante s ORDER BY s.nome" // JPQL
        )
})
public class Variante {
    @Version
    private int version;

    private static double G = 78.5;

    @Id
    private int codigo;

    @ManyToOne
    @JoinColumn(name="NOME_PRODUTO")
    @NotNull
    private Produto produto;

    @NotNull
    private String nome;

    private double weff_p;
    private double weff_n;
    private double ar;
    private double sigmaC;
    private double pp;

    //Propriedades do excel de cada variante
    @NotNull
    private double h_mm;
    @NotNull
    private double b_mm;
    @NotNull
    private double c_mm;
    @NotNull
    private double t_mm;
    @NotNull
    private double a_mm;
    @NotNull
    private double p_kg_m;
    @NotNull
    private double yg_mm;
    @NotNull
    private double zg_mm;
    @NotNull
    private double ly_mm;
    @NotNull
    private double wy_mm;
    @NotNull
    private double lz_mm;
    @NotNull
    private double wz_mm;
    @NotNull
    private double ys_mm;
    @NotNull
    private double zs_mm;
    @NotNull
    private double lt_mm;
    @NotNull
    private double lw_mm;


    @Lob
    private LinkedHashMap<Double,Double> mcr_p;
    @Lob
    private LinkedHashMap<Double,Double> mcr_n;


    @ManyToMany(mappedBy = "variantes")
    private List<Estrutura> estruturas;

    public Variante(){
        this.mcr_p = new LinkedHashMap<Double,Double>();
        this.mcr_n = new LinkedHashMap<Double,Double>();
        estruturas = new LinkedList<>();
    }

    public Variante(int codigo, @NotNull Produto produto, @NotNull String nome, double weff_p, double weff_n, double ar, double sigmaC, @NotNull double h_mm, @NotNull double b_mm, @NotNull double c_mm, @NotNull double t_mm, @NotNull double a_mm, @NotNull double p_kg_m, @NotNull double yg_mm, @NotNull double zg_mm, @NotNull double ly_mm, @NotNull double wy_mm, @NotNull double lz_mm, @NotNull double wz_mm, @NotNull double ys_mm, @NotNull double zs_mm, @NotNull double lt_mm, @NotNull double lw_mm) {
        this.codigo = codigo;
        this.produto = produto;
        this.nome = nome;
        this.weff_p = weff_p;
        this.weff_n = weff_n;
        this.ar = ar;
        this.sigmaC = sigmaC;
        this.pp =  G * ar * Math.pow(10, -6);
        this.h_mm = h_mm;
        this.b_mm = b_mm;
        this.c_mm = c_mm;
        this.t_mm = t_mm;
        this.a_mm = a_mm;
        this.p_kg_m = p_kg_m;
        this.yg_mm = yg_mm;
        this.zg_mm = zg_mm;
        this.ly_mm = ly_mm;
        this.wy_mm = wy_mm;
        this.lz_mm = lz_mm;
        this.wz_mm = wz_mm;
        this.ys_mm = ys_mm;
        this.zs_mm = zs_mm;
        this.lt_mm = lt_mm;
        this.lw_mm = lw_mm;

        this.mcr_p = new LinkedHashMap<Double,Double>();
        this.mcr_n = new LinkedHashMap<Double,Double>();
        estruturas = new LinkedList<>();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int code) {
        this.codigo = code;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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

    public double getH_mm() {
        return h_mm;
    }

    public void setH_mm(double h_mm) {
        this.h_mm = h_mm;
    }

    public double getB_mm() {
        return b_mm;
    }

    public void setB_mm(double b_mm) {
        this.b_mm = b_mm;
    }

    public double getC_mm() {
        return c_mm;
    }

    public void setC_mm(double c_mm) {
        this.c_mm = c_mm;
    }

    public double getT_mm() {
        return t_mm;
    }

    public void setT_mm(double t_mm) {
        this.t_mm = t_mm;
    }

    public double getA_mm() {
        return a_mm;
    }

    public void setA_mm(double a_mm) {
        this.a_mm = a_mm;
    }

    public double getP_kg_m() {
        return p_kg_m;
    }

    public void setP_kg_m(double p_kg_m) {
        this.p_kg_m = p_kg_m;
    }

    public double getYg_mm() {
        return yg_mm;
    }

    public void setYg_mm(double yg_mm) {
        this.yg_mm = yg_mm;
    }

    public double getZg_mm() {
        return zg_mm;
    }

    public void setZg_mm(double zg_mm) {
        this.zg_mm = zg_mm;
    }

    public double getLy_mm() {
        return ly_mm;
    }

    public void setLy_mm(double ly_mm) {
        this.ly_mm = ly_mm;
    }

    public double getWy_mm() {
        return wy_mm;
    }

    public void setWy_mm(double wy_mm) {
        this.wy_mm = wy_mm;
    }

    public double getLz_mm() {
        return lz_mm;
    }

    public void setLz_mm(double lz_mm) {
        this.lz_mm = lz_mm;
    }

    public double getWz_mm() {
        return wz_mm;
    }

    public void setWz_mm(double wz_mm) {
        this.wz_mm = wz_mm;
    }

    public double getYs_mm() {
        return ys_mm;
    }

    public void setYs_mm(double ys_mm) {
        this.ys_mm = ys_mm;
    }

    public double getZs_mm() {
        return zs_mm;
    }

    public void setZs_mm(double zs_mm) {
        this.zs_mm = zs_mm;
    }

    public double getLt_mm() {
        return lt_mm;
    }

    public void setLt_mm(double lt_mm) {
        this.lt_mm = lt_mm;
    }

    public double getLw_mm() {
        return lw_mm;
    }

    public void setLw_mm(double lw_mm) {
        this.lw_mm = lw_mm;
    }

    public List<Estrutura> getEstruturas() {
        return estruturas;
    }

    public void setEstruturas(List<Estrutura> estruturas) {
        this.estruturas = estruturas;
    }

    public void addEstrutura(Estrutura estrutura){
        this.estruturas.add(estrutura);
    }

    public void removeEstrutura(Estrutura estrutura){
        this.estruturas.remove(estrutura);
    }
}
