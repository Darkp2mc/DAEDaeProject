package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
        name = "PROJETOS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"NOME"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllProjetos",
                query = "SELECT s FROM Projeto s ORDER BY s.nome" // JPQL
        )
})
public class Projeto {


    @Version
    private int version;

    @Id
    private String nome;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "CLIENTE_USERNAME")
    private Cliente cliente;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "PROJETISTA_USERNAME")
    private Projetista projetista;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.REMOVE)
    private List<Estrutura> estruturas;

    private String comentario = "";

    @NotNull
    private int estado;

    @OneToMany(mappedBy = "projeto" ,cascade = CascadeType.REMOVE)
    @NotNull
    private List<Document> documents;

    public Projeto() {
        this.estruturas = new ArrayList<>();
        this.documents = new ArrayList<>();
    }

    public Projeto(String nome, @NotNull Cliente cliente, @NotNull Projetista projetista) {
        this.nome = nome;
        this.cliente = cliente;
        this.projetista = projetista;
        this.estruturas = new ArrayList<>();
        this.documents = new ArrayList<>();
        this.estado = 0;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Estrutura> getEstruturas() {
        return estruturas;
    }

    public void setEstruturas(List<Estrutura> estruturas) {
        this.estruturas = estruturas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Projetista getProjetista() {
        return projetista;
    }

    public void setProjetista(Projetista projetista) {
        this.projetista = projetista;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addEstrutura(Estrutura estrutura){
        this.estruturas.add(estrutura);
    }

    public void removeEstrutura(Estrutura estrutura){
        this.estruturas.remove(estrutura);
    }

    public int getEstado() {
        return estado;
    }

    public void rejeitar() {
        this.estado = -1;
    }

    public void terminar() {
        this.estado = 1;
    }

    public void addDocument(Document document){
        if (documents.contains(document) || document == null){
            return;
        }
        documents.add(document);
    }

    public void removeDocument(Document document){
        if (!documents.contains(document) || document == null){
            return;
        }
        documents.remove(document);
    }
}
