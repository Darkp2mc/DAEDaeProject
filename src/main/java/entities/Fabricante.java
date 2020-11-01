package entities;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public class Fabricante extends Pessoa{
    //Nome
    //Email
    //Lista de Projetos

    private LinkedList<Projeto> projetos;

    public Fabricante() {
        this.projetos = new LinkedList<Projeto>();
    }

    public Fabricante(String username, @NotNull String password, @NotNull String name, @NotNull @Email String email) {
        super(username, password, name, email);
        this.projetos = new LinkedList<Projeto>();
    }

    public LinkedList<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(LinkedList<Projeto> projetos) {
        this.projetos = projetos;
    }
}
