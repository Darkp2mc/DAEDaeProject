package ws;


import dtos.ProjetoDTO;
import ejbs.ProjetoBean;
import entities.Projetista;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/projetos")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProjetoService {

    @EJB
    ProjetoBean projetoBean;

    private ProjetoDTO toDTO(Projeto projeto){
        return new ProjetoDTO(projeto.getNome(),projeto.getCliente().getUsername(),projeto.getProjetista().getUsername());
    }
    private List<ProjetoDTO> toDTOS(List<Projeto> projetos) {
        return projetos.stream().map(this::toDTO).collect(Collectors.toList());

    }

    @GET
    @Path("{nome}")
    public Response getProjetoDetails(@PathParam("nome") String nome) throws MyEntityNotFoundException {
        Projeto projeto = projetoBean.findProjeto(nome);
        if(projeto!= null){
            return Response.status(Response.Status.OK)
                    .entity(toDTO(projeto))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("ERROR_FINDING_PROJETISTA")
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
}
