package dtos;

import entities.Pessoa;
import entities.Projeto;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class ProjetistaDTO extends PessoaDTO implements Serializable {

    List<Projeto> projetos;

    public ProjetistaDTO() {
        this.projetos = new LinkedList<Projeto>();
    }

    public ProjetistaDTO(String username, String password, String nome, String email) {
        super(username, password, nome, email);
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
