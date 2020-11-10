package ws;

import dtos.ProjetistaDTO;
import ejbs.ProjetistaBean;
import entities.Projetista;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
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

    private ProjetistaDTO toDTO(Projetista projetista){

        return new ProjetistaDTO(projetista.getUsername(),projetista.getPassword(),projetista.getName(),projetista.getEmail());
    }

    private List<ProjetistaDTO> toDTOS(List<Projetista> projetistas){
        return projetistas.stream().map(this::toDTO).collect(Collectors.toList());
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

    @PUT
    @Path("{username}")
    public Response updateProjetista(@PathParam("username") String username, ProjetistaDTO projetistaDTO) throws MyEntityNotFoundException{
        projetistaBean.update(username,
                projetistaDTO.getPassword(),
                projetistaDTO.getNome(),
                projetistaDTO.getEmail());
        Projetista projetista = projetistaBean.findProjetista(username);
        return Response.status(Response.Status.OK).entity(toDTO(projetista)).build();
    }


    @DELETE
    @Path("{username}")
    public Response deleteProjetista(@PathParam("username") String username, ProjetistaDTO projetistaDTO) throws MyEntityNotFoundException{
        projetistaBean.remove(username);
        return Response.status(Response.Status.OK).build();
    }
}
