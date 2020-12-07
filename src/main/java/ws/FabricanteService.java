package ws;

import dtos.FabricanteDTO;
import dtos.ProdutoDTO;
import dtos.ProjetistaDTO;
import dtos.ProjetoDTO;
import ejbs.FabricanteBean;
import ejbs.ProdutoBean;
import ejbs.ProjetistaBean;
import ejbs.ProjetoBean;
import entities.Fabricante;
import entities.Produto;
import entities.Projetista;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/fabricantes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class FabricanteService {
    @EJB
    private FabricanteBean fabricanteBean;

    @EJB
    private ProdutoBean produtoBean;




    private FabricanteDTO toDTO(Fabricante fabricante){


        FabricanteDTO fabricanteDTO = new FabricanteDTO(fabricante.getUsername(),fabricante.getPassword(),fabricante.getName(),fabricante.getEmail());

        fabricanteDTO.setProdutoDTOS(produtoDTOS(fabricante.getProdutos()));

        return fabricanteDTO;
    }

    private List<FabricanteDTO> toDTOS(List<Fabricante> fabricantes){
        return fabricantes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProdutoDTO produtoToDTO(Produto produto){
        return new ProdutoDTO(produto.getNome(),produto.getTipo(),produto.getFamilia(),produto.getFabricante().getName());
    }
    private List<ProdutoDTO> produtoDTOS(List<Produto> produtos) {
        return produtos.stream().map(this::produtoToDTO).collect(Collectors.toList());

    }

    @GET
    @Path("/")
    public List<FabricanteDTO> getAllFabricantesWS(){
        return toDTOS(fabricanteBean.getAllFabricantes());
    }

    @POST
    @Path("/")
    public Response createNewFabricante(FabricanteDTO fabricanteDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        fabricanteBean.create(fabricanteDTO.getUsername(),fabricanteDTO.getPassword(),fabricanteDTO.getNome(),fabricanteDTO.getEmail());

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("{username}")
    public Response getFabricanteDetails(@PathParam("username") String username){
        Fabricante fabricante = fabricanteBean.findFabricante(username);
        if(fabricante!= null){
            return Response.status(Response.Status.OK)
                    .entity(toDTO(fabricante))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_PROJETISTA")
                .build();
    }

    @GET
    @Path("{username}/produtos")
    public Response getFabricanteProdutos(@PathParam("username") String username){
        Fabricante fabricante = fabricanteBean.findFabricante(username);
        if(fabricante!= null ){
            return Response.status(Response.Status.OK)
                    .entity(produtoDTOS(fabricante.getProdutos()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_PROJETISTA")
                .build();
    }

    @DELETE
    @Path("{username}/produtos/{nome}")
    public Response deleteProduto(@PathParam("username") String username,final @PathParam("nome") String nome) throws MyEntityNotFoundException{

        Fabricante fabricante = fabricanteBean.findFabricante(username);
        if(fabricante== null){
            throw  new MyEntityNotFoundException("Fabricante com o username" + username+ "nao existe!");
        }
        Produto produto = produtoBean.findCProduto(nome);

        if (produto == null){
            throw new MyEntityNotFoundException("Produto com o nome" + nome+ "nao existe!");
        }

        fabricanteBean.removeProduto(produto);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{username}/produtos/{nome}")
    public Response updateProduto(@PathParam("username") String username, final @PathParam("nome") String nome, ProjetoDTO projetoDTO) throws  MyEntityNotFoundException{

        Fabricante fabricante = fabricanteBean.findFabricante(username);
        if(fabricante == null){
            throw  new MyEntityNotFoundException("Fabricante com o username" + username+ "nao existe!");
        }
        Produto produto = produtoBean.findCProduto(nome);

        if (produto == null){
            throw new MyEntityNotFoundException("Produto com o nome" + nome+ "nao existe!");
        }

        //fabricanteBean.i(nome,projetoDTO.getProjetistaUsername(),projetoDTO.getClienteUsername());

        return Response.status(Response.Status.OK).build();


    }


}
