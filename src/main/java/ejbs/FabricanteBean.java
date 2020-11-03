package ejbs;

import entities.Fabricante;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

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
}
