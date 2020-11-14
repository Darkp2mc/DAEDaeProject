package dtos;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ProjetoDTO implements Serializable {

    private String nome;
    private String clienteUsername;
    private String projetistaUsername;
    private List<EstruturaDTO> estruturas;
    private String comentario = "";

    public ProjetoDTO() {
        this.estruturas = new LinkedList<>();
    }

    public ProjetoDTO(String nome, String clienteUsername, String projetistaUsername) {
        this.nome = nome;
        this.clienteUsername = clienteUsername;
        this.projetistaUsername = projetistaUsername;
        this.estruturas = new LinkedList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClienteUsername() {
        return clienteUsername;
    }

    public void setClienteUsername(String clienteUsername) {
        this.clienteUsername = clienteUsername;
    }

    public String getProjetistaUsername() {
        return projetistaUsername;
    }

    public void setProjetistaUsername(String projetistaUsername) {
        this.projetistaUsername = projetistaUsername;
    }

    public List<EstruturaDTO> getEstruturas() {
        return estruturas;
    }

    public void setEstruturas(List<EstruturaDTO> estruturas) {
        this.estruturas = estruturas;
    }

    public void addEstruturaDTOs(EstruturaDTO estruturaDTO) {
        this.estruturas.add(estruturaDTO);
    }

    public void removeEstruturaDTOs(EstruturaDTO estruturaDTO) {
        this.estruturas.remove(estruturaDTO);
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
