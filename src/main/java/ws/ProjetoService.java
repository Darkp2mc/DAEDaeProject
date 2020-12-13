package ws;


import dtos.DocumentDTO;
import dtos.EmailDTO;
import dtos.EstruturaDTO;
import dtos.ProjetoDTO;
import ejbs.EmailBean;
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

        ProjetoDTO projetoDTO = new ProjetoDTO(projeto.getNome(),projeto.getCliente().getUsername(),projeto.getProjetista().getUsername(), projeto.isVisivel());
        projetoDTO.setEstado(projeto.getEstado());
        projetoDTO.setVisivel(projeto.isVisivel());
        projetoDTO.setDocumentos(documentDTOS(projeto.getDocuments()));
        projetoDTO.setComentario(projeto.getComentario());
        projetoDTO.setEstruturas(estruturaDTOS(projeto.getEstruturas()));
        return  projetoDTO;
    }

    private List<ProjetoDTO> toDTOS(List<Projeto> projetos) {
        return projetos.stream().map(this::toDTO).collect(Collectors.toList());

    }

    private EstruturaDTO estruturaDTO(Estrutura estrutura){
        EstruturaDTO estruturaDTO = new EstruturaDTO(estrutura.getNome(),estrutura.getTipoDeProduto(),estrutura.getProjeto().getNome(),
                estrutura.getNumeroDeVaos(), estrutura.getComprimentoDaVao(), estrutura.getAplicacao(),
                estrutura.getAlturaDaLage(), estrutura.getSobrecarga(), estrutura.getEstado());

        return estruturaDTO;
    }
    private List<EstruturaDTO> estruturaDTOS(List<Estrutura> estruturas){
        return estruturas.stream().map(this::estruturaDTO).collect(Collectors.toList());
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


    //TODO ao enviar o mail alterar a flag para o cliente poder ver
    @POST
    @Path("/{nome}/email/send")
    public Response sendEmail(@PathParam("nome") String nome, EmailDTO email) throws MyEntityNotFoundException, MessagingException {
        Projeto projeto = projetoBean.findProjeto(nome);
        if (projeto == null) {
            throw new MyEntityNotFoundException("Projeto com o nome '" + nome + "' não existe.");
        }
        emailBean.send(projeto.getCliente().getPessoaDeContacto().getEmail(), email.getSubject(), email.getMessage());
        projetoBean.tornarVisivel(projeto.getNome());
        return Response.status(Response.Status.OK).entity("E-mail sent").build();
    }

    @PUT
    @Path("{nome}/rejeitar")
    public Response reject(@PathParam("nome") String nome) throws MyEntityNotFoundException {
        Projeto projeto = projetoBean.findProjeto(nome);
        if (projeto == null) {
            throw new MyEntityNotFoundException("Projeto com o nome '" + nome + "' não existe.");
        }
        projetoBean.rejeitar(nome);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{nome}/aceitar")
    public Response aceitar(@PathParam("nome") String nome) throws MyEntityNotFoundException {
        Projeto projeto = projetoBean.findProjeto(nome);
        if (projeto == null){
            throw new MyEntityNotFoundException("Projeto com o nome '" + nome + "' não existe.");
        }

        projetoBean.aceitar(nome);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{nome}/terminar")
    public Response terminar(@PathParam("nome") String nome) throws MyEntityNotFoundException {
        Projeto projeto = projetoBean.findProjeto(nome);
        if (projeto == null) {
            throw new MyEntityNotFoundException("Projeto com o nome '" + nome + "' não existe.");
        }
        projetoBean.terminar(nome);
        return Response.status(Response.Status.OK).build();
    }

}
