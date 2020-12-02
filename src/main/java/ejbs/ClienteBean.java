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

        Cliente cliente = findCliente(username);
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

    public Cliente findCliente(String username){
        return manager.find(Cliente.class, username);
    }

    public List<Cliente> getAllClientes(){
        return manager.createNamedQuery("getAllClientes", Cliente.class).getResultList();
    }

    public void setComentario(String nome, String comentario) throws MyEntityNotFoundException, MyConstraintViolationException {

        Projeto projeto = manager.find(Projeto.class,nome);
        if (projeto== null){
            throw new MyEntityNotFoundException("Projeto nao encontrado");
        }

        try{
            manager.lock(projeto, LockModeType.OPTIMISTIC);
            projeto.setComentario(comentario);
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }


    }
}
