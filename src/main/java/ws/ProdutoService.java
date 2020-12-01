package ws;

import dtos.ProdutoDTO;
import dtos.VarianteDTO;
import ejbs.ProdutoBean;
import entities.Estrutura;
import entities.Produto;
import entities.Variante;

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

    private VarianteDTO varianteDTO(Variante variante){
        return new VarianteDTO(variante.getCodigo(),variante.getProduto().getNome(),variante.getNome(),variante.getWeff_p(),variante.getWeff_n(),variante.getAr(),variante.getSigmaC(),variante.getPp());
    }

    private List<VarianteDTO> varianteDTOS(List<Variante> variantes) {
        return variantes.stream().map(this::varianteDTO).collect(Collectors.toList());

    }

    @GET
    @Path("{name}/variantes")
    public Response getProdutoVariantes(@PathParam("name") String name){
        Produto produto = produtoBean.findCProduto(name);
        if(produto!= null ){
            return Response.status(Response.Status.OK)
                    .entity(varianteDTOS(produto.getVariantes()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_VARIANTES")
                .build();
    }
}
