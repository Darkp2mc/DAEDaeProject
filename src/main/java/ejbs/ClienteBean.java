package ejbs;

import entities.Cliente;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ClienteBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email, String morada ) throws MyEntityExistsException, MyConstraintViolationException {

        Cliente cliente = findCliente(username);
        if (cliente != null) {
            throw new MyEntityExistsException("Cliente já registado!!!");
        }
        try{
            cliente = new Cliente(username, password, nome, email, morada);
            manager.persist(cliente);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Cliente findCliente(String username){
        return manager.find(Cliente.class, username);
    }

    public List<Cliente> getAllClientes(){
        return manager.createNamedQuery("getAllClientes", Cliente.class).getResultList();
    }
}
