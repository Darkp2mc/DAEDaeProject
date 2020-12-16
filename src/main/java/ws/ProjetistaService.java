package ws;

import dtos.*;
import ejbs.EmailBean;
import ejbs.ProjetistaBean;
import ejbs.ProjetoBean;
import entities.Document;
import entities.Estrutura;
import entities.Projetista;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.security.auth.Subject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/projetistas")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})

public class ProjetistaService {

    @EJB
    private ProjetistaBean projetistaBean;

    @EJB
    private ProjetoBean projetoBean;

    @Context
    private SecurityContext securityContext;


    private ProjetistaDTO toDTO(Projetista projetista){
        ProjetistaDTO projetistaDTO= new ProjetistaDTO(projetista.getUsername(),projetista.getPassword(),projetista.getName(),projetista.getEmail());

        projetistaDTO.setProjetos(projetoDTOS(projetista.getProjetos()));

        return projetistaDTO;
    }

    private List<ProjetistaDTO> toDTOS(List<Projetista> projetistas){
        return projetistas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProjetoDTO projetoToDTO(Projeto projeto){
        ProjetoDTO projetoDTO= new ProjetoDTO(projeto.getNome(),
                projeto.getCliente().getUsername(),projeto.getProjetista().getUsername(),projeto.isVisivel(), projeto.getEstado());

        projetoDTO.setDocumentos(documentDTOS(projeto.getDocuments()));
        projetoDTO.setComentario(projeto.getComentario());
        projetoDTO.setEstruturas(estruturaDTOS(projeto.getEstruturas()));

        return projetoDTO;
    }
    private List<ProjetoDTO> projetoDTOS(List<Projeto> projetos) {
        return projetos.stream().map(this::projetoToDTO).collect(Collectors.toList());

    }

    private List<DocumentDTO> documentDTOS(List<Document> documents){
        return  documents.stream().map(this::documentDTO).collect(Collectors.toList());
    }

    private DocumentDTO documentDTO(Document document){
        return new DocumentDTO(document.getId(),document.getFilepath(),document.getFilename());
    }

    private EstruturaDTO estruturaDTO(Estrutura estrutura){
        EstruturaDTO estruturaDTO=  new EstruturaDTO(estrutura.getNome(),estrutura.getTipoDeProduto(),estrutura.getProjeto().getNome(),estrutura.getNumeroDeVaos(),estrutura.getComprimentoDaVao(),estrutura.getAplicacao(),estrutura.getAlturaDaLage(),estrutura.getAlturaDaLage(),estrutura.getEstado());

        return estruturaDTO;
    }

    private List<EstruturaDTO> estruturaDTOS(List<Estrutura> estruturas){
        return estruturas.stream().map(this::estruturaDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<ProjetistaDTO> getAllProjetistasWS(){
        return toDTOS(projetistaBean.getAllProjetistas());
    }

    @POST
    @Path("/")
    public Response createNewProjetista(ProjetistaDTO projetistaDTO) throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {

        projetistaBean.create(projetistaDTO.getUsername(),projetistaDTO.getPassword(),projetistaDTO.getNome(),projetistaDTO.getEmail());

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("{username}")
    public Response getProjetistaDetails(@PathParam("username") String username){
        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista")&&
                principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Projetista projetista = projetistaBean.findProjetista(username);
        if(projetista!= null){
            return Response.status(Response.Status.OK)
                    .entity(toDTO(projetista))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_PROJETISTA")
                .build();
    }

    @GET
    @Path("{username}/projetos")
    public Response getProjetistaProjects(@PathParam("username") String username){
        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista")&&
                principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Projetista projetista = projetistaBean.findProjetista(username);
        if(projetista!= null ){
            return Response.status(Response.Status.OK)
                    .entity(projetoDTOS(projetista.getProjetos()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_PROJETISTA")
                .build();
    }

    @DELETE
    @Path("{username}/projetos/{nome}")
    public Response deleteProjeto(@PathParam("username") String username,final @PathParam("nome") String nome) throws MyEntityNotFoundException{

        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista")&&
                        principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Projetista projetista = projetistaBean.findProjetista(username);
        if(projetista== null){
            throw  new MyEntityNotFoundException("Projetista com o username" + username+ "nao existe!");
        }
        Projeto projeto = projetoBean.findProjeto(nome);

        if (projeto == null){
            throw new MyEntityNotFoundException("projeto com o nome" + nome+ "nao existe!");
        }

        projetistaBean.removeProjeto(projeto);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{username}/projetos/{nome}")
    public Response updateProjeto(@PathParam("username") String username, final @PathParam("nome") String nome, ProjetoDTO projetoDTO) throws  MyEntityNotFoundException{

        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista")&&
                principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Projetista projetista = projetistaBean.findProjetista(username);
        if(projetista== null){
            throw  new MyEntityNotFoundException("Projetista com o username" + username+ "nao existe!");
        }
        Projeto projeto = projetoBean.findProjeto(nome);

        if (projeto == null){
            throw new MyEntityNotFoundException("projeto com o nome" + nome+ "nao existe!");
        }

        projetistaBean.updateProjeto(nome,projetoDTO.getProjetistaUsername(),projetoDTO.getClienteUsername());

        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("{username}/projetos/{nome}")
    public Response getProjeto(@PathParam("username") String username, final @PathParam("nome") String nome) throws  MyEntityNotFoundException{

        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista") &&
                        principal.getName().equals(username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Projetista projetista = projetistaBean.findProjetista(username);
        if(projetista== null){
            throw  new MyEntityNotFoundException("Projetista com o username " + username+ " nao existe!");
        }
        Projeto projeto = projetoBean.findProjeto(nome);

        if (projeto == null){
            throw new MyEntityNotFoundException("projeto com o nome " + nome+ " nao existe!");
        }

        System.out.println(projeto.getEstado()+ "aqui");
        return Response.status(Response.Status.OK)
                .entity(projetoToDTO(projeto))
                .build();
    }






}
