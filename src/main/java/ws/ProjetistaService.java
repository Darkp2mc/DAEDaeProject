package ws;

import dtos.EmailDTO;
import dtos.ProjetistaDTO;
import dtos.ProjetoDTO;
import ejbs.EmailBean;
import ejbs.ProjetistaBean;
import ejbs.ProjetoBean;
import entities.Projetista;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.security.auth.Subject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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




    private ProjetistaDTO toDTO(Projetista projetista){


        ProjetistaDTO projetistaDTO= new ProjetistaDTO(projetista.getUsername(),projetista.getPassword(),projetista.getName(),projetista.getEmail());

        projetistaDTO.setProjetos(projetoDTOS(projetista.getProjetos()));

        return projetistaDTO;
    }

    private List<ProjetistaDTO> toDTOS(List<Projetista> projetistas){
        return projetistas.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProjetoDTO projetoToDTO(Projeto projeto){
        return new ProjetoDTO(projeto.getNome(),projeto.getCliente().getUsername(),projeto.getProjetista().getUsername());
    }
    private List<ProjetoDTO> projetoDTOS(List<Projeto> projetos) {
        return projetos.stream().map(this::projetoToDTO).collect(Collectors.toList());

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
    public Response updateProjeto(@PathParam("username") String username, final @PathParam("nome") String nome) throws  MyEntityNotFoundException{

        Projetista projetista = projetistaBean.findProjetista(username);
        if(projetista== null){
            throw  new MyEntityNotFoundException("Projetista com o username" + username+ "nao existe!");
        }
        Projeto projeto = projetoBean.findProjeto(nome);

        if (projeto == null){
            throw new MyEntityNotFoundException("projeto com o nome" + nome+ "nao existe!");
        }

        projetistaBean.updateProjeto(projeto.getNome(),projeto.getProjetista().getUsername(),projeto.getCliente().getUsername());

        return Response.status(Response.Status.OK).build();


    }




}
