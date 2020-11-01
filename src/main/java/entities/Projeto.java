package entities;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;

public class Projeto {

    //nome
    //cliente
    //lista de ficheiros
    //lista de estruturas
    //comentarios do cliente

    @Id
    private String nome;
    @NotNull
    private Cliente cliente;

    private LinkedList<Estrutura> estruturas;

    private String comentario;

    public Projeto() {
        this.estruturas = new LinkedList<>();
    }

    public Projeto(String nome, @NotNull Cliente cliente) {
        this.nome = nome;
        this.cliente = cliente;
        this.estruturas = new LinkedList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LinkedList<Estrutura> getEstruturas() {
        return estruturas;
    }

    public void setEstruturas(LinkedList<Estrutura> estruturas) {
        this.estruturas = estruturas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
