package dtos;

import entities.Estrutura;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class FabricanteDTO extends PessoaDTO implements Serializable {

    private List<ProdutoDTO> produtoDTOS;

    public FabricanteDTO() {
        this.produtoDTOS = new LinkedList<>();
    }

    public FabricanteDTO(String username, String password, String nome, String email) {
        super(username, password, nome, email);
        this.produtoDTOS = new LinkedList<>();
    }

    public List<ProdutoDTO> getProdutoDTOS() {
        return produtoDTOS;
    }

    public void setProdutoDTOS(List<ProdutoDTO> produtoDTOS) {
        this.produtoDTOS = produtoDTOS;
    }

    public void addProdutoDTO(ProdutoDTO produtoDTO){
        this.produtoDTOS.add(produtoDTO);
    }

    public void removeProdutoDTO(ProdutoDTO produtoDTO){
        this.produtoDTOS.remove(produtoDTO);
    }
}
