package dtos;

import entities.Pessoa;
import entities.Projeto;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class ProjetistaDTO extends PessoaDTO implements Serializable {

    List<ProjetoDTO> projetoDTOs;

    public ProjetistaDTO() {
        this.projetoDTOs = new LinkedList<ProjetoDTO>();
    }

    public ProjetistaDTO(String username, String password, String nome, String email) {
        super(username, password, nome, email);
        this.projetoDTOs = new LinkedList<ProjetoDTO>();
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
