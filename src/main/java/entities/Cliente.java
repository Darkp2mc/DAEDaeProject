package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(
        name = "CLIENTES",
        uniqueConstraints = @UniqueConstraint(columnNames = {"NOME"})
)
@NamedQueries({
        @NamedQuery(
                name = "getAllClientes",
                query = "SELECT s FROM Cliente s ORDER BY s.nome" // JPQL
        )
})
public class Cliente extends Pessoa{

    @NotNull
    private String morada;
    @OneToOne
    @JoinColumn(name = "PESSOA_DE_CONTACTO")
    private PessoaDeContacto pessoaDeContacto;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.REMOVE)
    private List<Projeto> projetos;


    public Cliente() {
        this.projetos = new LinkedList<>();
    }

    public Cliente(String username, @NotNull String password, @NotNull String name, @NotNull @Email String email, @NotNull String morada, @NotNull PessoaDeContacto pessoaDeContacto) {
        super(username, password, name, email);
        this.morada = morada;
        this.pessoaDeContacto = pessoaDeContacto;
        this.projetos = new LinkedList<>();
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public Pessoa getPessoaDeContacto() {
        return pessoaDeContacto;
    }

    public void setPessoaDeContacto(PessoaDeContacto pessoaDeContacto) {
        this.pessoaDeContacto = pessoaDeContacto;
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
