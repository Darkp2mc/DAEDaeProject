package dtos;

import entities.Pessoa;
import entities.Projeto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ClienteDTO extends PessoaDTO implements Serializable {

    private String morada;
    private String nomePessoaDeContacto;
    private List<Projeto> projetos;

    public ClienteDTO() {
        this.projetos = new LinkedList<>();
    }

    public ClienteDTO(String username, String password, String nome, String email, String morada, String nomePessoaDeContacto) {
        super(username, password, nome, email);
        this.morada = morada;
        this.nomePessoaDeContacto = nomePessoaDeContacto;
        this.projetos = new LinkedList<>();
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getNomePessoaDeContacto() {
        return nomePessoaDeContacto;
    }

    public void setNomePessoaDeContacto(String nomePessoaDeContacto) {
        this.nomePessoaDeContacto = nomePessoaDeContacto;
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
