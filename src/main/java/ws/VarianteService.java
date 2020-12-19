package ws;

import dtos.ProdutoDTO;
import dtos.VarianteDTO;
import ejbs.ProdutoBean;
import ejbs.VarianteBean;
import entities.Produto;
import entities.Variante;
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

@Path("/variantes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class VarianteService {

    @EJB
    private VarianteBean varianteBean;

    @Context
    private SecurityContext securityContext;


    private VarianteDTO toDTO(Variante variante){
        return new VarianteDTO(variante.getCodigo(), variante.getProduto().getNome(), variante.getNome(), variante.getWeff_p(), variante.getWeff_n(), variante.getAr(), variante.getSigmaC(), variante.getH_mm(), variante.getB_mm(), variante.getC_mm(), variante.getT_mm(), variante.getA_mm(), variante.getP_kg_m(), variante.getYg_mm(), variante.getZg_mm(), variante.getLy_mm(), variante.getWy_mm(), variante.getLz_mm(), variante.getWz_mm(), variante.getYs_mm(), variante.getZs_mm(), variante.getLt_mm(), variante.getLw_mm());
    }

    private List<VarianteDTO> toDTOS(List<Variante> variantes){
        return variantes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<VarianteDTO> getAllVariantesWS(){
        return toDTOS(varianteBean.getAllVariantes());
    }



    @POST
    @Path("/")
    public Response createNewVariante(VarianteDTO varianteDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        varianteBean.create(varianteDTO.getCodigo(), varianteDTO.getProdutoNome(),varianteDTO.getNome(),varianteDTO.getWeff_p(), varianteDTO.getWeff_n(), varianteDTO.getAr(), varianteDTO.getSigmaC(), varianteDTO.getH_mm(), varianteDTO.getB_mm(), varianteDTO.getC_mm(), varianteDTO.getT_mm(), varianteDTO.getA_mm(), varianteDTO.getP_kg_m(), varianteDTO.getYg_mm(), varianteDTO.getZg_mm(), varianteDTO.getLy_mm(), varianteDTO.getWy_mm(), varianteDTO.getLz_mm(), varianteDTO.getWz_mm(), varianteDTO.getYs_mm(), varianteDTO.getZs_mm(), varianteDTO.getLt_mm(), varianteDTO.getLw_mm());

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("{codigo}")
    public Response getVarianteDetails(@PathParam("codigo") int codigo) throws MyEntityNotFoundException {

        Variante variante = varianteBean.getVariante(codigo);

        Principal principal = securityContext.getUserPrincipal();
        if(!((securityContext.isUserInRole("Fabricante")) && variante.getProduto().getFabricante().getUsername().equals(principal.getName()))){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.OK)
                .entity(toDTO(variante))
                .build();

    }


}
