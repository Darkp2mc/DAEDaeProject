package ws;

import dtos.EstruturaDTO;
import dtos.ProdutoDTO;
import ejbs.EstruturaBean;
import ejbs.ProdutoBean;
import entities.Estrutura;
import entities.Produto;
import exceptions.*;

import javax.ejb.EJB;
import javax.security.auth.Subject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/projetistas")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class EstruturaService {
    @EJB
    private EstruturaBean estruturaBean;

    @EJB
    private ProdutoBean produtoBean;

    private EstruturaDTO toDTO(Estrutura estrutura){


        EstruturaDTO estruturaDTO= new EstruturaDTO(estrutura.getNome(), estrutura.getTipoDeProduto(), estrutura.getProjeto().getNome());

        estruturaDTO.setProdutoDTOS(produtoDTOS(estrutura.getProdutos()));

        return estruturaDTO;
    }

    private List<EstruturaDTO> toDTOS(List<Estrutura> estruturas){
        return estruturas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProdutoDTO projetoToDTO(Produto produto){
        return new ProdutoDTO(produto.getNome(), produto.getFabricante().getName());
    }
    private List<ProdutoDTO> produtoDTOS(List<Produto> produtos) {
        return produtos.stream().map(this::projetoToDTO).collect(Collectors.toList());

    }

    @GET
    @Path("/")
    public List<EstruturaDTO> getAllEstruturasWS(){
        return toDTOS(estruturaBean.getAllEstruturas());
    }

    @POST
    @Path("/")
    public Response createNewEstrutura(EstruturaDTO estruturaDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        estruturaBean.create(estruturaDTO.getNome(), estruturaDTO.getTipoDeProduto(), "something", estruturaDTO.getProjetoNome());

        return Response.status(Response.Status.CREATED).build();
    }
}
