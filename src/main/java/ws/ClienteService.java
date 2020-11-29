package ws;


import dtos.ClienteDTO;
import dtos.ProjetoDTO;
import ejbs.ClienteBean;
import ejbs.ProjetoBean;
import entities.Cliente;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    private ProjetoDTO projetoToDTO(Projeto projeto){
        return new ProjetoDTO(projeto.getNome(),projeto.getCliente().getUsername(),projeto.getProjetista().getUsername());
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

    @GET
    @Path("/")
    public List<ClienteDTO> getAllProjetistasWS(){
        return clienteDTOS(clienteBean.getAllClientes());
    }

    @GET
    @Path("{username}")
    public Response getClienteDetails(@PathParam("username") String username) throws MyEntityNotFoundException, MyConstraintViolationException {
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
}
