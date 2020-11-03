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

    @OneToMany (mappedBy = "fabricante", cascade = CascadeType.REMOVE)
    private List<Estrutura> estruturas;

    public Fabricante() {
        this.estruturas = new LinkedList<>();
    }

    public Fabricante(String username, @NotNull String password, @NotNull String name, @NotNull @Email String email) {
        super(username, password, name, email);
        this.estruturas = new LinkedList<>();
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
