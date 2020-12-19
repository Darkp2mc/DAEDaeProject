package ejbs;

import entities.Cliente;
import entities.PessoaDeContacto;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ClienteBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email, String morada, String usernamePC ) throws MyEntityExistsException, MyEntityNotFoundException,MyConstraintViolationException {

        Cliente cliente = manager.find(Cliente.class,username);
        if (cliente != null) {
            throw new MyEntityExistsException("Cliente já registado!!!");
        }
        PessoaDeContacto pessoaDeContacto = manager.find(PessoaDeContacto.class,usernamePC);
        if (pessoaDeContacto == null){
            throw new MyEntityNotFoundException("Pessoa de contacto não existe");
        }

        try{
            cliente = new Cliente(username, password, nome, email, morada,pessoaDeContacto);
            manager.persist(cliente);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Cliente findCliente(String username) throws MyEntityNotFoundException {

        Cliente cliente = manager.find(Cliente.class, username);
        if (cliente != null){
            return cliente;
        }

        throw new MyEntityNotFoundException("Cliente com o username " + username+ " nao existe!");

    }

    public List<Cliente> getAllClientes(){
        return manager.createNamedQuery("getAllClientes", Cliente.class).getResultList();
    }


}
