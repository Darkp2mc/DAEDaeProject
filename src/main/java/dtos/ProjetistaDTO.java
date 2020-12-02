package dtos;

import entities.Pessoa;
import entities.Projeto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class ProjetistaDTO extends PessoaDTO implements Serializable {

    List<ProjetoDTO> projetos;

    public ProjetistaDTO() {
        this.projetos = new ArrayList<>();
    }

    public ProjetistaDTO(String username, String password, String nome, String email) {
        super(username, password, nome, email);
        this.projetos = new ArrayList<>();
    }

    public List<ProjetoDTO> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<ProjetoDTO> projetoDTOs) {
        this.projetos = projetoDTOs;
    }


}
