package ws;


import dtos.DocumentDTO;
import dtos.EmailDTO;
import dtos.ProjetoDTO;
import ejbs.EmailBean;
import ejbs.ProjetoBean;
import entities.Document;
import entities.Projetista;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/projetos")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProjetoService {

    @EJB
    ProjetoBean projetoBean;

    @EJB
    private EmailBean emailBean;

    @Context
    private SecurityContext securityContext;

    private ProjetoDTO toDTO(Projeto projeto){

        ProjetoDTO projetoDTO = new ProjetoDTO(projeto.getNome(),projeto.getCliente().getUsername(),projeto.getProjetista().getUsername());
        projetoDTO.setDocumentos(documentDTOS(projeto.getDocuments()));
        projetoDTO.setComentario(projeto.getComentario());
        return  projetoDTO;
    }

    private List<ProjetoDTO> toDTOS(List<Projeto> projetos) {
        return projetos.stream().map(this::toDTO).collect(Collectors.toList());

    }

    private DocumentDTO documentDTO(Document document){
        return new DocumentDTO(document.getId(),document.getFilepath(),document.getFilename());
    }

    private List<DocumentDTO> documentDTOS(List<Document> documents){
        return  documents.stream().map(this::documentDTO).collect(Collectors.toList());
    }

    @GET
    @Path("{nome}")
    public Response getProjetoDetails(@PathParam("nome") String nome) throws MyEntityNotFoundException {

        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista") ||
                securityContext.isUserInRole("Cliente") &&
                        principal.getName().equals(nome))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Projeto projeto = projetoBean.findProjeto(nome);
        if(projeto== null){
            throw new MyEntityNotFoundException("Projeto com o nome" + nome + "nao existe!");
        }
        return Response.status(Response.Status.OK)
                .entity(toDTO(projeto))
                .build();

    }

    @POST
    @Path("/")
    public Response createProjeto( ProjetoDTO projetoDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {

        Projeto projeto = projetoBean.findProjeto(projetoDTO.getNome());
        if (projeto!= null){
            throw new MyEntityExistsException("O projeto com o nome" + projetoDTO.getNome()+ " ja existe!");
        }
        projetoBean.create(projetoDTO.getNome(),projetoDTO.getClienteUsername(),projetoDTO.getProjetistaUsername());

        return Response.status(Response.Status.CREATED).build();

    }

    @POST
    @Path("/{nome}/email/send")
    public Response sendEmail(@PathParam("nome") String nome, EmailDTO email) throws MyEntityNotFoundException, MessagingException {
        Projeto projeto = projetoBean.findProjeto(nome);
        if (projeto == null) {
            throw new MyEntityNotFoundException("Projeto com o nome '" + nome + "' não existe.");
        }
        emailBean.send(projeto.getCliente().getPessoaDeContacto().getEmail(), email.getSubject(), email.getMessage());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }
}
