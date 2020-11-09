package dtos;

import entities.Pessoa;
import entities.Projeto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ClienteDTO extends PessoaDTO implements Serializable {

    private String morada;
    private String pessoaDeContactoUsername;
    private List<ProjetoDTO> projetoDTOs;

    public ClienteDTO() {
        this.projetoDTOs = new LinkedList<>();
    }

    public ClienteDTO(String username, String password, String nome, String email, String morada, String pessoaDeContactoUsername) {
        super(username, password, nome, email);
        this.morada = morada;
        this.pessoaDeContactoUsername = pessoaDeContactoUsername;
        this.projetoDTOs = new LinkedList<>();
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getPessoaDeContactoUsername() {
        return pessoaDeContactoUsername;
    }

    public void setPessoaDeContactoUsername(String pessoaDeContactoUsername) {
        this.pessoaDeContactoUsername = pessoaDeContactoUsername;
    }

    public List<ProjetoDTO> getProjetoDTOs() {
        return projetoDTOs;
    }

    public void setProjetoDTOs(List<ProjetoDTO> projetoDTOs) {
        this.projetoDTOs = projetoDTOs;
    }

    public void removeProjetoDTO(ProjetoDTO projetoDTO){
        projetoDTOs.remove(projetoDTO);
    }

    public void addProjetoDTO(ProjetoDTO projetoDTO){
        projetoDTOs.add(projetoDTO);
    }
}
