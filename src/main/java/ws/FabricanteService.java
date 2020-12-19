package ws;

import dtos.*;
import ejbs.*;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.LinkedList;
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

    @EJB
    private VarianteBean varianteBean;

    @Context
    private SecurityContext securityContext;

    private FabricanteDTO toDTO(Fabricante fabricante){


        FabricanteDTO fabricanteDTO = new FabricanteDTO(fabricante.getUsername(),fabricante.getPassword(),fabricante.getName(),fabricante.getEmail());

        fabricanteDTO.setProdutoDTOS(produtoDTOS(fabricante.getProdutos()));

        return fabricanteDTO;
    }

    private List<FabricanteDTO> toDTOS(List<Fabricante> fabricantes){
        return fabricantes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProdutoDTO produtoToDTO(Produto produto){
        ProdutoDTO produtoDTO= new ProdutoDTO(produto.getNome(), produto.getTipo(), produto.getFamilia(),produto.getE(), produto.getN(), produto.getG(), produto.getFabricante().getUsername());
        produtoDTO.setVarianteDTOs(varianteDTOS(produto.getVariantes()));
        return produtoDTO;
    }
    private List<ProdutoDTO> produtoDTOS(List<Produto> produtos) {
        return produtos.stream().map(this::produtoToDTO).collect(Collectors.toList());

    }

    private VarianteDTO varianteToDTO(Variante variante){
        return new VarianteDTO(variante.getCodigo(), variante.getProduto().getNome(), variante.getNome(), variante.getWeff_p(), variante.getWeff_n(), variante.getAr(), variante.getSigmaC(), variante.getH_mm(), variante.getB_mm(), variante.getC_mm(), variante.getT_mm(), variante.getA_mm(), variante.getP_kg_m(), variante.getYg_mm(), variante.getZg_mm(), variante.getLy_mm(), variante.getWy_mm(), variante.getLz_mm(), variante.getWz_mm(), variante.getYs_mm(), variante.getZs_mm(), variante.getLt_mm(), variante.getLw_mm());
    }
    private List<VarianteDTO> varianteDTOS(List<Variante> variantes) {
        return variantes.stream().map(this::varianteToDTO).collect(Collectors.toList());

    }

    //GET todos os fabricantes
    @GET
    @Path("/")
    public List<FabricanteDTO> getAllFabricantesWS(){
        return toDTOS(fabricanteBean.getAllFabricantes());
    }

    //GET detalhes de um fabricante
    @GET
    @Path("{username}")
    public Response getFabricanteDetails(@PathParam("username") String username) throws MyEntityNotFoundException {
        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.status(Response.Status.OK)
                .entity(toDTO(fabricante))
                .build();

    }

    //GET todos os produtos do fabricante "username"
    @GET
    @Path("{username}/produtos")
    public Response getFabricanteProdutos(@PathParam("username") String username) throws MyEntityNotFoundException {
        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.status(Response.Status.OK)
                .entity(produtoDTOS(fabricante.getProdutos()))
                .build();

    }

    //GET detalhes do produto "nome" do fabricante "username"
    @GET
    @Path("{username}/produtos/{nome}")
    public Response getFabricanteProdutoDetails(@PathParam("username") String username, @PathParam("nome") String nome) throws  MyEntityNotFoundException{
        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Produto produto = produtoBean.findCProduto(nome);
        if (produto.getFabricante().getUsername().equals(fabricante.getUsername())){
            return Response.status(Response.Status.OK)
                    .entity(produtoToDTO(produto))
                    .build();
        }

        return Response.status(Response.Status.CONFLICT)
                .entity("O fabricante não pode aceder a este produto")
                .build();

    }

    //UPDATE do produto "nome" do fabricante "username"
    @PUT
    @Path("{username}/produtos/{nome}")
    public Response updateProduto(@PathParam("username") String username, final @PathParam("nome") String nome, ProdutoDTO produtoDTO) throws  MyEntityNotFoundException{

        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Produto produto = produtoBean.findCProduto(nome);

        if (!produto.getFabricante().getUsername().equals(username)){
            throw new MyEntityNotFoundException("Produto com o nome " + nome+ " nao existe para fabricante "+username+"!");
        }

        fabricanteBean.updateProduto(nome,produtoDTO.getTipo(), produtoDTO.getFamilia(),produtoDTO.getE(), produtoDTO.getN(), produtoDTO.getG(),produtoDTO.getFabricanteUsername());

        return Response.status(Response.Status.OK).build();

    }

    //DELETE do produto "nome" do fabricante "username"
    @DELETE
    @Path("{username}/produtos/{nome}")
    public Response deleteProduto(@PathParam("username") String username,final @PathParam("nome") String nome) throws MyEntityNotFoundException{

        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Produto produto = produtoBean.findCProduto(nome);

        fabricanteBean.removeProduto(produto);
        return Response.status(Response.Status.OK).build();
    }

    //GET todas as variantes do produto "nome" do fabricante "username"
    @GET
    @Path("{username}/produtos/{nome}/variantes")
    public Response getVariantesProduto(@PathParam("username") String username, final @PathParam("nome") String nome) throws  MyEntityNotFoundException{

        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Produto produto = produtoBean.findCProduto(nome);

        if (!produto.getVariantes().isEmpty()){
            return Response.status(Response.Status.OK)
                    .entity(varianteDTOS(produto.getVariantes()))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_VARIANTES")
                .build();
    }


    //DELETE da variante "codigo" do produto "nome" do fabricante "username"
    @DELETE
    @Path("{username}/produtos/{nome}/variantes/{codigo}")
    public Response deleteVariante(@PathParam("username") String username,final @PathParam("nome") String nome, @PathParam("codigo") int codigo) throws MyEntityNotFoundException{

        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Produto produto = produtoBean.findCProduto(nome);

        Variante variante = varianteBean.getVariante(codigo);

        //fabricanteBean.removeVariante(produto, variante);
        produtoBean.removeVariante(variante);
        return Response.status(Response.Status.OK).build();
    }


    //GET variante "codigo" do produto "nome" do fabricante "username"
    @GET
    @Path("{username}/produtos/{nome}/variantes/{codigo}")
    public Response getVarianteProduto(@PathParam("username") String username, @PathParam("nome") String nome, final @PathParam("codigo") int codigo) throws  MyEntityNotFoundException{

        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Produto produto = produtoBean.findCProduto(nome);

        Variante variante = varianteBean.getVariante(codigo);

        return Response.status(Response.Status.OK)
                .entity(varianteToDTO(variante))
                .build();

    }

    //UPDATE da variante "codigo" do produto "nome" do fabricante "username"
    @PUT
    @Path("{username}/produtos/{nome}/variantes/{codigo}")
    public Response updateVariante(@PathParam("username") String username, final @PathParam("nome") String nome, final @PathParam("codigo") int codigo, VarianteDTO varianteDTO) throws  MyEntityNotFoundException{
        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Produto produto = produtoBean.findCProduto(nome);

        Variante variante = varianteBean.getVariante(codigo);

        produtoBean.updateVariante(codigo, nome,varianteDTO.getNome(), varianteDTO.getWeff_p(), varianteDTO.getWeff_n(), varianteDTO.getAr(), varianteDTO.getSigmaC(), varianteDTO.getH_mm(), varianteDTO.getB_mm(), varianteDTO.getC_mm(), varianteDTO.getT_mm(), varianteDTO.getA_mm(), varianteDTO.getP_kg_m(), varianteDTO.getYg_mm(), varianteDTO.getZg_mm(), varianteDTO.getLy_mm(), varianteDTO.getWy_mm(), varianteDTO.getLz_mm(), varianteDTO.getWz_mm(), varianteDTO.getYs_mm(), varianteDTO.getZs_mm(), varianteDTO.getLt_mm(), varianteDTO.getLw_mm());

        return Response.status(Response.Status.OK).build();
    }


    @GET
    @Path("{username}/variantesEmEstruturas")
    public Response getMateriaisEmEstruturasAdjudicadas(@PathParam("username") String username) throws MyEntityNotFoundException {
        Fabricante fabricante = fabricanteBean.findFabricante(username);

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Fabricante") && principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }


        List<Variante> variantes = new LinkedList<>();

        for (Produto produto : fabricante.getProdutos()) {
            for (Variante variante: produto.getVariantes()) {
                for (Estrutura estrutura: variante.getEstruturas()) {
                    if (estrutura.getEstado() == 1) {
                        variantes.add(variante);
                        break;
                    }
                }
            }
        }

        if (variantes.size() > 0){
            return Response.status(Response.Status.OK)
                    .entity(varianteDTOS(variantes))
                    .build();
        }


        return Response.status(Response.Status.NO_CONTENT)
                .entity("Sem variantes a serem usadas em estruturas")
                .build();


    }

}
