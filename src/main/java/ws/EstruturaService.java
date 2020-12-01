package ws;

import dtos.EstruturaDTO;
import dtos.ProdutoDTO;
import dtos.VarianteDTO;
import ejbs.EstruturaBean;
import ejbs.ProdutoBean;
import ejbs.SimulacaoBean;
import ejbs.VarianteBean;
import entities.Estrutura;
import entities.Produto;
import entities.Variante;
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

    @EJB
    private SimulacaoBean simulacaoBean;

    @EJB
    private VarianteBean varianteBean;

    private EstruturaDTO toDTO(Estrutura estrutura){
        EstruturaDTO estruturaDTO= new EstruturaDTO(estrutura.getNome(),estrutura.getTipoDeProduto(),estrutura.getProjeto().getNome(),
                                                estrutura.getNumeroDeVaos(), estrutura.getComprimentoDaVao(), estrutura.getAplicacao(),
                                                estrutura.getAlturaDaLage(), estrutura.getSobrecarga());
        estruturaDTO.setVarianteDTOs(varianteDTOS(estrutura.getVariantes()));
        return estruturaDTO;
    }

    private List<EstruturaDTO> toDTOS(List<Estrutura> estruturas){
        return estruturas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private VarianteDTO varianteToDTO(Variante variante){
        return new VarianteDTO(variante.getCodigo(), variante.getProduto().getNome(),variante.getNome(),variante.getWeff_p(),variante.getWeff_n(),variante.getAr(),variante.getSigmaC(),variante.getPp());
    }
    private List<VarianteDTO> varianteDTOS(List<Variante> variantes) {
        return variantes.stream().map(this::varianteToDTO).collect(Collectors.toList());

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
                estruturaDTO.getNumeroDeVaos(), estruturaDTO.getComprimentoDaVao(), estruturaDTO.getAplicacao(),
                estruturaDTO.getAlturaDaLage(), estruturaDTO.getSobrecarga());

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
                .entity("ERROR_FINDING_ESTRUTURA")
                .build();
    }

    @GET
    @Path("{name}/variantes")
    public Response getEstruturaVariantes(@PathParam("name") String name){
        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura!= null ){
            return Response.status(Response.Status.OK)
                    .entity(varianteDTOS(estrutura.getVariantes()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_ESTRUTURA")
                .build();
    }

    @DELETE
    @Path("{name}/variantes/{varianteCodigo}")
    public Response removeProduto(@PathParam("name") String name,@PathParam("varianteCodigo") int varianteCodigo) throws MyEntityNotFoundException{

        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura== null){
            throw  new MyEntityNotFoundException("Estrutura com o nome" + name+ "nao existe!");
        }
        Variante variante = varianteBean.getVariante(varianteCodigo);

        if (variante == null){
            throw new MyEntityNotFoundException("Variante com o codigo " + varianteCodigo+ " nao existe!");
        }

        estrutura.removeVariante(variante);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{name}/variantes/{varianteCodigo}")
    public Response addVariante(@PathParam("name") String name,final @PathParam("varianteCodigo") int varianteCodigo) throws MyEntityNotFoundException, MyIllegalArgumentException {

        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura== null){
            throw  new MyEntityNotFoundException("Estrutura com o nome" + name+ "nao existe!");
        }
        Variante variante = varianteBean.getVariante(varianteCodigo);

        if (variante == null){
            throw new MyEntityNotFoundException("Variante com o codigo " + varianteCodigo+ " nao existe!");
        }

        estruturaBean.addVariante(name,varianteCodigo);
        return Response.status(Response.Status.OK).build();
    }
}
