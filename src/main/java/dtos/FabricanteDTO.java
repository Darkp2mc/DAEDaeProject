package dtos;

import entities.Estrutura;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class FabricanteDTO extends PessoaDTO implements Serializable {

    private List<Estrutura> estruturas;

    public FabricanteDTO() {
        this.estruturas = new LinkedList<>();
    }

    public FabricanteDTO(String username, String password, String nome, String email) {
        super(username, password, nome, email);
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
