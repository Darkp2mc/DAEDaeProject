package entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "document")
@NamedQuery(name = "getProjectDocuments", query = "SELECT doc FROM Document doc WHERE doc.projeto.nome = :nome")

public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String filepath;

    @NotNull
    private String filename;

    @ManyToOne
    @JoinColumn(name = "PROJETO_NOME")
    @NotNull
    private Projeto projeto;




    public Document(String filepath, String filename, Projeto projeto) {
        this.filepath = filepath;
        this.filename = filename;
        this.projeto = projeto;
    }

    public Document(){

    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
