package ws;


import dtos.ClienteDTO;
import dtos.DocumentDTO;
import dtos.EstruturaDTO;
import dtos.ProjetoDTO;
import ejbs.ClienteBean;
import ejbs.EmailBean;
import ejbs.ProjetoBean;
import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/clientes")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ClienteService {

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private ProjetoBean projetoBean;

    @EJB
    private EmailBean emailBean;

    @Context
    private SecurityContext securityContext;

    //TODO alterar este metodo para devolver so os projetos que o cliente pode ver
    private ProjetoDTO projetoToDTO(Projeto projeto){
        ProjetoDTO projetoDTO= new ProjetoDTO(projeto.getNome(),projeto.getCliente().getUsername(),projeto.getProjetista().getUsername());
        projetoDTO.setDocumentos(documentDTOS(projeto.getDocuments()));
        projetoDTO.setEstruturas(estruturaDTOS(projeto.getEstruturas()));
        return  projetoDTO;
    }
    private List<ProjetoDTO> projetoDTOS(List<Projeto> projetos) {
        return projetos.stream().map(this::projetoToDTO).collect(Collectors.toList());

    }
    private ClienteDTO clienteDTO(Cliente cliente){
        ClienteDTO clienteDTO = new ClienteDTO(cliente.getUsername(),cliente.getPassword(),cliente.getName(),cliente.getEmail(), cliente.getMorada(),cliente.getPessoaDeContacto().getUsername());
        clienteDTO.setProjetoDTOs(projetoDTOS(cliente.getProjetos()));

        return  clienteDTO;
    }

    private List<ClienteDTO> clienteDTOS(List<Cliente> clientes){
        return clientes.stream().map(this::clienteDTO).collect(Collectors.toList());
    }



    private List<DocumentDTO> documentDTOS(List<Document> documents){
        return  documents.stream().map(this::documentDTO).collect(Collectors.toList());
    }

    private DocumentDTO documentDTO(Document document){
        return new DocumentDTO(document.getId(),document.getFilepath(),document.getFilename());
    }

    private EstruturaDTO estruturaDTO(Estrutura estrutura){
        EstruturaDTO estruturaDTO=  new EstruturaDTO(estrutura.getNome(),estrutura.getTipoDeProduto(),estrutura.getProjeto().getNome(),estrutura.getNumeroDeVaos(),estrutura.getComprimentoDaVao(),estrutura.getAplicacao(),estrutura.getAlturaDaLage(),estrutura.getAlturaDaLage());

        return estruturaDTO;
    }

    private List<EstruturaDTO> estruturaDTOS(List<Estrutura> estruturas){
        return estruturas.stream().map(this::estruturaDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<ClienteDTO> getAllProjetistasWS(){
        return clienteDTOS(clienteBean.getAllClientes());
    }

    @GET
    @Path("{username}")
    public Response getClienteDetails(@PathParam("username") String username) throws MyEntityNotFoundException, MyConstraintViolationException {

        Principal principal = securityContext.getUserPrincipal();
        if(!securityContext.isUserInRole("Cliente") &&
                principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Cliente cliente = clienteBean.findCliente(username);

        if (cliente== null){
            throw new MyEntityNotFoundException("Cliente nao Encontrado");
        }
        try {
            return Response.status(Response.Status.OK)
                    .entity(clienteDTO(cliente))
                    .build();
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }

    }

    @GET
    @Path("{username}/projetos/{nome}")
    public Response getProjeto(@PathParam("username") String username, final @PathParam("nome") String nome) throws  MyEntityNotFoundException{

        Principal principal = securityContext.getUserPrincipal();
        if((!securityContext.isUserInRole("Cliente") )&&
                        principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Cliente cliente = clienteBean.findCliente(username);
        if(cliente== null){
            throw  new MyEntityNotFoundException("Cliente com o username " + username+ " nao existe!");
        }
        Projeto projeto = projetoBean.findProjeto(nome);

        if (projeto == null){
            throw new MyEntityNotFoundException("Projeto com o nome " + nome+ " nao existe!");
        }

        return Response.status(Response.Status.OK)
                .entity(projetoToDTO(projeto))
                .build();
    }

    @POST
    @Path("{username}/projetos/{nome}/comentario")
    public Response makeComment(@PathParam("username") String username, final @PathParam("nome") String nome, ProjetoDTO projetoDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();
        if((!securityContext.isUserInRole("Cliente") )&&
                principal.getName().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Cliente cliente = clienteBean.findCliente(username);
        if(cliente== null){
            throw  new MyEntityNotFoundException("Cliente com o username " + username+ " nao existe!");
        }
        Projeto projeto = projetoBean.findProjeto(nome);

        if (projeto == null){
            throw new MyEntityNotFoundException("Projeto com o nome " + nome+ " nao existe!");
        }

        try{
            clienteBean.setComentario(nome,projetoDTO.getComentario());
            emailBean.send(projeto.getProjetista().getEmail(),"Informaçao sobre projeto", projetoDTO.getComentario());


            return Response.status(Response.Status.OK).build();
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }




    }






}
