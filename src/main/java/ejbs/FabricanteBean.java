package ejbs;

import entities.*;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class FabricanteBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email) throws MyEntityExistsException, MyConstraintViolationException {

        Fabricante fabricante = findFabricante(username);
        if (fabricante != null) {
            throw new MyEntityExistsException("Fabricante já registado!!!");
        }
        try{
            fabricante = new Fabricante(username, password, nome, email);
            manager.persist(fabricante);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Fabricante findFabricante(String username){
        return manager.find(Fabricante.class, username);
    }

    public List<Fabricante> getAllFabricantes(){
        return manager.createNamedQuery("getAllFabricantes", Fabricante.class).getResultList();
    }

    public void removeProduto(Produto produto) throws MyEntityNotFoundException {

        Produto p = manager.find(Produto.class,produto.getNome());

        if(p==null){
            throw new MyEntityNotFoundException("Produto nao existe!!");
        }

        Fabricante fabricante = p.getFabricante();

        if (fabricante==null){
            throw new MyEntityNotFoundException("Fabricante com o nome "+fabricante.getName()+" não existe");
        }

        fabricante.removeProduto(p);
        manager.remove(p);



    }
}
