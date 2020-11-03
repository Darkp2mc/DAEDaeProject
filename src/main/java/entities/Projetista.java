package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
        name = "PROJETISTAS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"USERNAME"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllProjetistas",
                query = "SELECT s FROM Projetista s ORDER BY s.nome" // JPQL
        )
})
public class Projetista  extends Pessoa{

    @OneToMany(mappedBy = "projetista", cascade = CascadeType.REMOVE)
    private List<Projeto> projetos;

    public Projetista() {
        this.projetos = new LinkedList<Projeto>();
    }

    public Projetista(String username, @NotNull String password, @NotNull String name, @NotNull @Email String email) {
        super(username, password, name, email);
        this.projetos = new LinkedList<Projeto>();
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    public void removeProjeto(Projeto projeto){
        projetos.remove(projeto);
    }

    public void addProjeto(Projeto projeto){
        projetos.add(projeto);
    }
}
