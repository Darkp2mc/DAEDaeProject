package ws;

import dtos.ProdutoDTO;
import dtos.ProjetistaDTO;
import ejbs.ProdutoBean;
import entities.Produto;
import entities.Projetista;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/produtos")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class ProdutoService {

    @EJB
    private ProdutoBean produtoBean;


    private ProdutoDTO toDTO(Produto produto){

        ProdutoDTO produtoDTO = new ProdutoDTO(produto.getNome(), produto.getFabricante().getName());
        //ProdutoDTO produtoDTO = new ProdutoDTO(produto.getNome(), produto.getDimensoes(),produto.getFamilia(), produto.getTipo(), produto.getFabricante().getName());

        produtoDTO.setFamilia(produtoDTO.getFamilia());
        produtoDTO.setTipo(produtoDTO.getTipo());
        produtoDTO.setDimensoes(produtoDTO.getDimensoes());

        return produtoDTO;
    }

    private List<ProdutoDTO> toDTOS(List<Produto> produtos){
        return produtos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<ProdutoDTO> getAllProdutosWS(){
        return toDTOS(produtoBean.getAllProdutos());
    }

    @POST
    @Path("/")
    public Response createNewProduto(ProdutoDTO produtoDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        produtoBean.create(produtoDTO.getNome(),produtoDTO.getFabricanteNome());

        return Response.status(Response.Status.CREATED).build();
    }
}
