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

@Path("/estruturas")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class EstruturaService {
    @EJB
    private EstruturaBean estruturaBean;

    @EJB
    private ProdutoBean produtoBean;

    private EstruturaDTO toDTO(Estrutura estrutura){
        EstruturaDTO estruturaDTO= new EstruturaDTO(estrutura.getNome(),estrutura.getTipoDeProduto(),estrutura.getProjeto().getNome(),
                                                estrutura.getNumeroDeVaos(), estrutura.getComprimentoDaVao(), estrutura.getAplicacao(), estrutura.getAlturaDaLage());
        estruturaDTO.setProdutoDTOS(produtoDTOS(estrutura.getProdutos()));
        return estruturaDTO;
    }

    private List<EstruturaDTO> toDTOS(List<Estrutura> estruturas){
        return estruturas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProdutoDTO produtoToDTO(Produto produto){
        return new ProdutoDTO(produto.getNome(), produto.getFabricante().getName());
    }
    private List<ProdutoDTO> produtoDTOS(List<Produto> produtos) {
        return produtos.stream().map(this::produtoToDTO).collect(Collectors.toList());

    }

    @GET
    @Path("/")
    public List<EstruturaDTO> getAllEstruturasWS(){
        return toDTOS(estruturaBean.getAllEstruturas());
    }

    @POST
    @Path("/")
    public Response createNewEstrutura(EstruturaDTO estruturaDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException, MyIllegalArgumentException {
        estruturaBean.create(estruturaDTO.getNome(),estruturaDTO.getProjetoNome(), estruturaDTO.getTipoDeProduto(),
                estruturaDTO.getNumeroDeVaos(), estruturaDTO.getComprimentoDaVao(), estruturaDTO.getAplicacao(), estruturaDTO.getAlturaDaLage());

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("{name}")
    public Response getEstruturaDetails(@PathParam("name") String name){
        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura!= null){
            return Response.status(Response.Status.OK)
                    .entity(toDTO(estrutura))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_PROJETISTA")
                .build();
    }

    @GET
    @Path("{name}/produto")
    public Response getEstruturaProdutos(@PathParam("name") String name){
        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura!= null ){
            return Response.status(Response.Status.OK)
                    .entity(produtoDTOS(estrutura.getProdutos()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_PROJETISTA")
                .build();
    }

    @DELETE
    @Path("{name}/produtos/{produtoName}")
    public Response deleteProduto(@PathParam("name") String name,final @PathParam("produtoName") String produtoName) throws MyEntityNotFoundException{

        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura== null){
            throw  new MyEntityNotFoundException("Estrutura com o nome" + name+ "nao existe!");
        }
        Produto produto = produtoBean.findCProduto(produtoName);

        if (produto == null){
            throw new MyEntityNotFoundException("Produto com o nome" + produtoName+ "nao existe!");
        }

        estrutura.removeProduto(produto);
        return Response.status(Response.Status.OK).build();
    }
}
