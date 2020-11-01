package entities;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public class Cliente extends Pessoa{
    //Nome
    //Pessoa de contacto(pessoa que comunica com o cliente)
    //Morada
    //Email
    //Lista de Projetos

    @NotNull
    private String morada;
    @NotNull
    private Pessoa pessoaDeContacto;
    private LinkedList<Projeto> projetos;


    public Cliente() {
        this.projetos = new LinkedList<Projeto>();
    }

    public Cliente(String username, @NotNull String password, @NotNull String name, @NotNull @Email String email, @NotNull String morada, @NotNull Pessoa pessoaDeContacto) {
        super(username, password, name, email);
        this.morada = morada;
        this.pessoaDeContacto = pessoaDeContacto;
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

    public void setPessoaDeContacto(Pessoa pessoaDeContacto) {
        this.pessoaDeContacto = pessoaDeContacto;
    }

    public LinkedList<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(LinkedList<Projeto> projetos) {
        this.projetos = projetos;
    }
}
