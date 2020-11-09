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
    private List<Produto> produtos;

    public Fabricante() {
        this.produtos = new LinkedList<>();
    }

    public Fabricante(String username, @NotNull String password, @NotNull String name, @NotNull @Email String email) {
        super(username, password, name, email);
        this.produtos = new LinkedList<>();
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public void addProduto(Produto produto){
        this.produtos.add(produto);
    }

    public void removeProduto(Produto produto){
        this.produtos.remove(produto);
    }
}
