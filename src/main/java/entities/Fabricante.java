package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(
        name = "FABRICANTES",
        uniqueConstraints = @UniqueConstraint(columnNames = {"NOME"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllFabricantes",
                query = "SELECT s FROM Fabricante s ORDER BY s.nome" // JPQL
        )
})
public class Fabricante extends Pessoa{

    @ManyToMany
    @JoinColumn(name = "FABRICANTE_USERNAME")
    private List<Projeto> projetos;

    public Fabricante() {
        this.projetos = new LinkedList<Projeto>();
    }

    public Fabricante(String username, @NotNull String password, @NotNull String name, @NotNull @Email String email) {
        super(username, password, name, email);
        this.projetos = new LinkedList<Projeto>();
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }
}
