package ws;

import dtos.ProdutoDTO;
import dtos.VarianteDTO;
import ejbs.EstruturaBean;
import ejbs.ProdutoBean;
import ejbs.SimulacaoBean;
import ejbs.VarianteBean;
import entities.Estrutura;
import entities.Produto;
import entities.Variante;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/produtos")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class ProdutoService {

    @EJB
    private ProdutoBean produtoBean;

    @EJB
    private SimulacaoBean simulacaoBean;

    @EJB
    private VarianteBean varianteBean;

    @EJB
    private EstruturaBean estruturaBean;

    private VarianteDTO varianteDTO(Variante variante){
        return new VarianteDTO(variante.getCodigo(),variante.getProduto().getNome(),variante.getNome(),variante.getWeff_p(),variante.getWeff_n(),variante.getAr(),variante.getSigmaC(),variante.getPp());
    }

    private List<VarianteDTO> varianteDTOS(List<Variante> variantes) {
        return variantes.stream().map(this::varianteDTO).collect(Collectors.toList());

    }

    @GET
    @Path("{name}/estruturas/{estruturaNome}/variantes")
    public Response getProdutoVariantes(@PathParam("name") String name,@PathParam("estruturaNome") String estruturaNome) throws MyEntityNotFoundException {
        Produto produto = produtoBean.findCProduto(name);
        if (produto == null)
            throw  new MyEntityNotFoundException("Produto com o nome " + name+ " nao existe!");
        Estrutura estrutura = estruturaBean.findEstrutura(estruturaNome);
        if(estrutura== null){
            throw  new MyEntityNotFoundException("Estrutura com o nome" + estruturaNome+ "nao existe!");
        }
        List<Variante> variantes = produto.getVariantes();
        if (produto.getFamilia().equals("C") || produto.getFamilia().equals("Z"))
            variantes.removeIf(v -> !simulacaoBean.simulaVariante(Integer.parseInt(estrutura.getNumeroDeVaos()),
                    Double.parseDouble(estrutura.getComprimentoDaVao()),
                    Integer.parseInt(estrutura.getSobrecarga()), v));

        return Response.status(Response.Status.OK)
                    .entity(varianteDTOS(variantes))
                    .build();
    }
}
