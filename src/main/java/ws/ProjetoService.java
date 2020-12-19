package ws;


import dtos.DocumentDTO;
import dtos.EmailDTO;
import dtos.EstruturaDTO;
import dtos.ProjetoDTO;
import ejbs.EmailBean;
import ejbs.EstruturaBean;
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
import javax.validation.ConstraintViolationException;
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
    private EstruturaBean estruturaBean;

    @EJB
    private EmailBean emailBean;

    @Context
    private SecurityContext securityContext;

    private ProjetoDTO toDTO(Projeto projeto){

        ProjetoDTO projetoDTO = new ProjetoDTO(projeto.getNome(),projeto.getCliente().getUsername(),projeto.getProjetista().getUsername(), projeto.isVisivel(),projeto.getEstado());
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

        Projeto projeto = projetoBean.findProjeto(nome);
        if(projeto== null){
            throw new MyEntityNotFoundException("Projeto com o nome" + nome + "nao existe!");
        }

        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista") && projeto.getProjetista().getUsername().equals(principal.getName())) ||
                (securityContext.isUserInRole("Cliente") && projeto.getProjetista().getUsername().equals(principal.getName()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.status(Response.Status.OK)
                .entity(toDTO(projeto))
                .build();

    }

    @POST
    @Path("/")
    public Response createProjeto( ProjetoDTO projetoDTO) throws MyEntityNotFoundException, MyEntityExistsException, MyConstraintViolationException {
        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Projeto projeto = projetoBean.findProjeto(projetoDTO.getNome());
        if (projeto!= null){
            throw new MyEntityExistsException("O projeto com o nome" + projetoDTO.getNome()+ " ja existe!");
        }
        projetoBean.create(projetoDTO.getNome(),projetoDTO.getClienteUsername(),projetoDTO.getProjetistaUsername());

        return Response.status(Response.Status.CREATED).build();

    }

    //TODO Quem é que aceita e rejeita projetos?
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

    //TODO segurança aqui-so cliente e que pode
    @PUT
    @Path("{nome}/rejeitar")
    public Response reject(@PathParam("nome") String nome, ProjetoDTO projetoDTO) throws MyEntityNotFoundException, MyConstraintViolationException {
        Projeto projeto = projetoBean.findProjeto(nome);
        if (projeto == null) {
            throw new MyEntityNotFoundException("Projeto com o nome '" + nome + "' não existe.");
        }


        if (projeto.getEstado()!= 2){
            try{
                projetoBean.rejeitar(nome);
                projetoBean.setComentario(nome,projetoDTO.getComentario());
                emailBean.send(projeto.getCliente().getPessoaDeContacto().getEmail(),"Informaçao sobre o Projeto " + nome, projetoDTO.getComentario());
                return Response.status(Response.Status.OK).build();
            } catch (ConstraintViolationException e){
                throw new MyConstraintViolationException(e);
            }
        }
        return Response.status(Response.Status.CONFLICT).entity("Impossivel rejeitar um projeto ja terminado").build();
    }

    //TODO segurança aqui-so cliente e que pode
    @PUT
    @Path("{nome}/aceitar")
    public Response aceitar(@PathParam("nome") String nome, ProjetoDTO projetoDTO) throws MyEntityNotFoundException, MyConstraintViolationException {


        Projeto projeto = projetoBean.findProjeto(nome);
        if (projeto == null){
            throw new MyEntityNotFoundException("Projeto com o nome '" + nome + "' não existe.");
        }

        if (projeto.getEstado()!=2) {

            try{
                projetoBean.aceitar(nome);
                projetoBean.setComentario(nome,projetoDTO.getComentario());
                emailBean.send(projeto.getCliente().getPessoaDeContacto().getEmail(),"Informaçao sobre o Projeto " + nome, projetoDTO.getComentario());
                return Response.status(Response.Status.OK).build();
            }
            catch (ConstraintViolationException e){
                throw new MyConstraintViolationException(e);
            }

        }
        return Response.status(Response.Status.CONFLICT).entity("Impossivel aceitar um projeto ja terminado").build();

    }

    @PUT
    @Path("{nome}/terminar")
    public Response terminar(@PathParam("nome") String nome,EmailDTO email) throws MyEntityNotFoundException, MyConstraintViolationException {
        Projeto projeto = projetoBean.findProjeto(nome);
        if (projeto == null) {
            throw new MyEntityNotFoundException("Projeto com o nome '" + nome + "' não existe.");
        }

        Principal principal = securityContext.getUserPrincipal();
        if(!(securityContext.isUserInRole("Projetista") && principal.getName().equals(projeto.getProjetista().getUsername()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(projeto.getEstado()!=2){
            try{
                emailBean.send(projeto.getCliente().getPessoaDeContacto().getEmail(),"Projeto concluido", "Caro "+projeto.getCliente().getPessoaDeContacto().getName()+" envio o presente email para o informar que o seu projeto" +
                        "com o nome " + nome + " foi dado como Concluido! Cumprimentos, " + projeto.getProjetista().getName());
                projetoBean.terminar(nome);
                return Response.status(Response.Status.OK).build();
            }catch (ConstraintViolationException e){
                throw new MyConstraintViolationException(e);
            }
        }
        return Response.status(Response.Status.CONFLICT).entity("Impossivel terminar um projeto ja terminado").build();

    }

    @PUT
    @Path("{projetoNome}/estrutura/{estruturaNome}/remover")
    public Response removeEstrutura(@PathParam("projetoNome") String projetoNome, @PathParam("estruturaNome") String estruturaNome) throws MyEntityNotFoundException {

        Projeto projeto = projetoBean.findProjeto(projetoNome);
        if (projeto == null) {
            throw new MyEntityNotFoundException("Projeto com o nome '" + projetoNome + "' não existe.");
        }

        Estrutura estrutura = estruturaBean.findEstrutura(estruturaNome);
        if(estrutura== null){
            throw  new MyEntityNotFoundException("Estrutura com o nome" + estruturaNome+ "nao existe!");
        }

        Principal principal = securityContext.getUserPrincipal();
        if (!(securityContext.isUserInRole("Projetista") &&
                projeto.getProjetista().getUsername().equals(principal.getName()))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        projetoBean.removerEstrutura(projeto,estrutura);
        return Response.status(Response.Status.OK).build();
    }

}
