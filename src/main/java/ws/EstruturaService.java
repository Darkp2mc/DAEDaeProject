package ws;

import dtos.EstruturaDTO;
import dtos.ProdutoDTO;
import dtos.VarianteDTO;
import ejbs.*;
import entities.Estrutura;
import entities.Produto;
import entities.Variante;
import exceptions.*;

import javax.ejb.EJB;
import javax.security.auth.Subject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.LinkedList;
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

    @EJB
    private EmailBean emailBean;

    @Context
    private SecurityContext securityContext;

    private EstruturaDTO toDTO(Estrutura estrutura){

        EstruturaDTO estruturaDTO= new EstruturaDTO(estrutura.getNome(),estrutura.getTipoDeProduto(),estrutura.getProjeto().getNome(),
                                                estrutura.getNumeroDeVaos(), estrutura.getComprimentoDaVao(), estrutura.getAplicacao(),
                                                estrutura.getAlturaDaLage(), estrutura.getSobrecarga(), estrutura.getEstado()
        );

        estruturaDTO.setVarianteDTOs(varianteDTOS(estrutura.getVariantes()));
        return estruturaDTO;
    }

    private List<EstruturaDTO> toDTOS(List<Estrutura> estruturas){
        return estruturas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private VarianteDTO varianteToDTO(Variante variante){
        return new VarianteDTO(variante.getCodigo(), variante.getProduto().getNome(),variante.getNome(),variante.getWeff_p(),variante.getWeff_n(),variante.getAr(),variante.getSigmaC(),variante.getH_mm(),variante.getB_mm(),variante.getC_mm(),variante.getT_mm(),variante.getA_mm(), variante.getP_kg_m(),variante.getYg_mm(),variante.getZg_mm(),variante.getLy_mm(),variante.getWy_mm(),variante.getLz_mm(),variante.getWz_mm(),variante.getYs_mm(),variante.getZs_mm(), variante.getLt_mm(),variante.getLw_mm());
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
        if(estrutura!= null) {

            Principal principal = securityContext.getUserPrincipal();
            /*if(!((securityContext.isUserInRole("Cliente") || securityContext.isUserInRole("Projetista")) &&
                    (estrutura.getProjeto().getCliente().getName().equals(principal.getName()) ||
                     estrutura.getProjeto().getProjetista().getName().equals(principal.getName())))){*/
            if (!((securityContext.isUserInRole("Cliente") &&
                    estrutura.getProjeto().getCliente().getUsername().equals(principal.getName())) ||
                    (securityContext.isUserInRole("Projetista") &&
                            estrutura.getProjeto().getProjetista().getUsername().equals(principal.getName())))) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

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

            Principal principal = securityContext.getUserPrincipal();
            /*if(!((securityContext.isUserInRole("Cliente") || securityContext.isUserInRole("Projetista")) &&
                    (estrutura.getProjeto().getCliente().getName().equals(principal.getName()) ||
                     estrutura.getProjeto().getProjetista().getName().equals(principal.getName())))){*/
            if (!((securityContext.isUserInRole("Cliente") &&
                    estrutura.getProjeto().getCliente().getUsername().equals(principal.getName())) ||
                    (securityContext.isUserInRole("Projetista") &&
                            estrutura.getProjeto().getProjetista().getUsername().equals(principal.getName())))) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            return Response.status(Response.Status.OK)
                    .entity(varianteDTOS(estrutura.getVariantes()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_ESTRUTURA")
                .build();
    }

    @PUT
    @Path("{name}/variantes/{varianteCodigo}/remove")
    public Response removeProduto(@PathParam("name") String name,@PathParam("varianteCodigo") int varianteCodigo) throws MyEntityNotFoundException{

        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura== null){
            throw  new MyEntityNotFoundException("Estrutura com o nome" + name+ "nao existe!");
        }
        Variante variante = varianteBean.getVariante(varianteCodigo);

        if (variante == null){
            throw new MyEntityNotFoundException("Variante com o codigo " + varianteCodigo+ " nao existe!");
        }

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Projetista") &&
                        estrutura.getProjeto().getProjetista().getUsername().equals(principal.getName()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        estruturaBean.removeVariante(name,varianteCodigo);
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

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Projetista") &&
                estrutura.getProjeto().getProjetista().getUsername().equals(principal.getName()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        estruturaBean.addVariante(name,varianteCodigo);
        return Response.status(Response.Status.OK).build();
    }


    @PUT
    @Path("{name}/rejeitar")
    public Response rejeitar(@PathParam("name") String name) throws MyEntityNotFoundException {
        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura== null){
            throw new MyEntityNotFoundException("Estrutura com o nome" + name+ "nao existe!");
        }

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Cliente") &&
                estrutura.getProjeto().getCliente().getUsername().equals(principal.getName()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        estruturaBean.rejeitar(name);
        emailBean.send(estrutura.getProjeto().getProjetista().getEmail(),"Estrutura Rejeita" , "Caro " + estrutura.getProjeto().getProjetista().getName()+ " envio o seguinte email para o informar que a estrutura "+ estrutura.getNome() +
                "foi recusada. Envie-me mensagem para discutir-mos possiveis alterações. Cumprimentos, " + estrutura.getProjeto().getCliente().getName());

        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{name}/aceitar")
    public Response aceitar(@PathParam("name") String name) throws MyEntityNotFoundException {
        Estrutura estrutura = estruturaBean.findEstrutura(name);
        if(estrutura== null){
            throw new MyEntityNotFoundException("Estrutura com o nome" + name+ "nao existe!");
        }

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Cliente") &&
                estrutura.getProjeto().getCliente().getUsername().equals(principal.getName()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        estruturaBean.aceitar(name);
        emailBean.send(estrutura.getProjeto().getProjetista().getEmail(),"Estrutura Aceite" , "Caro " + estrutura.getProjeto().getProjetista().getName()+ " envio o seguinte email para o informar que a estrutura "+ estrutura.getNome() +
                "foi aceite. Pode começar a sua construçao.  Cumprimentos, " + estrutura.getProjeto().getCliente().getName());

        return Response.status(Response.Status.OK).build();
    }


    @GET
    @Path("{estruturaNome}/variantes/simulation")
    public Response getVariantes(@PathParam("estruturaNome") String estruturaNome) throws MyEntityNotFoundException {
        Estrutura estrutura = estruturaBean.findEstrutura(estruturaNome);
        if(estrutura== null){
            throw  new MyEntityNotFoundException("Estrutura com o nome" + estruturaNome+ "nao existe!");
        }
        //lista de todos os produtos do mesmo tipo da estrutura
        List<Produto> produtos = produtoBean.getAllProdutos();
        produtos.removeIf(p -> !p.getTipo().equals(estrutura.getTipoDeProduto()));

        List<Variante> variantes = new LinkedList<>();
        for (Produto p : produtos){
            variantes.addAll(p.getVariantes());
        }

        for (Variante v : variantes){
            boolean flag = false;


            if ((v.getProduto().getFamilia().equals("C") || v.getProduto().getFamilia().equals("Z")))
                if (!simulacaoBean.simulaVariante(Integer.parseInt(estrutura.getNumeroDeVaos()),
                        Double.parseDouble(estrutura.getComprimentoDaVao()),
                        Integer.parseInt(estrutura.getSobrecarga()),v))
                    variantes.remove(v);
                else
                    for (Variante vv : estrutura.getVariantes())
                        if (vv.getCodigo() == v.getCodigo()) {
                            flag = true;
                            variantes.remove(v);
                            break;
                        }
        }

        return Response.status(Response.Status.OK)
                .entity(varianteDTOS(variantes))
                .build();
    }
}
