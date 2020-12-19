package ws;

import dtos.ProdutoDTO;
import dtos.ProjetoDTO;
import dtos.VarianteDTO;
import ejbs.FabricanteBean;
import ejbs.ProdutoBean;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/produtos")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class ProdutoService {

    @EJB
    private ProdutoBean produtoBean;

    @EJB
    private FabricanteBean fabricanteBean;

    @Context
    private SecurityContext securityContext;


    private ProdutoDTO toDTO(Produto produto){
        ProdutoDTO produtoDTO= new ProdutoDTO(produto.getNome(), produto.getTipo(), produto.getFamilia(),produto.getE(), produto.getN(), produto.getG(), produto.getFabricante().getUsername());
        produtoDTO.setVarianteDTOs(varianteDTOS(produto.getVariantes()));
        return produtoDTO;
    }

    private List<ProdutoDTO> toDTOS(List<Produto> produtos){
        return produtos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private VarianteDTO varianteToDTO(Variante variante){
        return new VarianteDTO(variante.getCodigo(), variante.getProduto().getNome(), variante.getNome(), variante.getWeff_p(), variante.getWeff_n(), variante.getAr(), variante.getSigmaC(), variante.getH_mm(), variante.getB_mm(), variante.getC_mm(), variante.getT_mm(), variante.getA_mm(), variante.getP_kg_m(), variante.getYg_mm(), variante.getZg_mm(), variante.getLy_mm(), variante.getWy_mm(), variante.getLz_mm(), variante.getWz_mm(), variante.getYs_mm(), variante.getZs_mm(), variante.getLt_mm(), variante.getLw_mm());
    }
    private List<VarianteDTO> varianteDTOS(List<Variante> variantes) {
        return variantes.stream().map(this::varianteToDTO).collect(Collectors.toList());

    }

    @GET
    @Path("/")
    public List<ProdutoDTO> getAllProdutosWS(){
        return toDTOS(produtoBean.getAllProdutos());
    }

    //CREATE um novo produto
    @POST
    @Path("/")
    public Response createNewProduto(ProdutoDTO produtoDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(produtoDTO.getFabricanteUsername()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        produtoBean.create(produtoDTO.getNome(),produtoDTO.getTipo(),produtoDTO.getFamilia(),produtoDTO.getE(), produtoDTO.getN(), produtoDTO.getFabricanteUsername());

        return Response.status(Response.Status.CREATED).build();
    }

    //GET detalhes do produto "nome"

    @GET
    @Path("{nome}")
    public Response getProdutoDetails(@PathParam("nome") String nome) throws MyEntityNotFoundException {

        Produto produto = produtoBean.findCProduto(nome);

        Principal principal = securityContext.getUserPrincipal();
        Fabricante fabricante = produto.getFabricante();
        if(!(securityContext.isUserInRole("Fabricante") && fabricante.getUsername().equals(principal.getName()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.status(Response.Status.OK)
                .entity(toDTO(produto))
                .build();

    }

    //GET variantes do produto "nome"
    @GET
    @Path("{nome}/variantes")
    public Response getProdutoVariantes(@PathParam("nome") String nome) throws MyEntityNotFoundException {

        Produto produto = produtoBean.findCProduto(nome);

        Principal principal = securityContext.getUserPrincipal();
        Fabricante fabricante = produto.getFabricante();
        if(!(securityContext.isUserInRole("Fabricante") && fabricante.getUsername().equals(principal.getName()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.OK)
                .entity(varianteDTOS(produto.getVariantes()))
                .build();

    }


}
