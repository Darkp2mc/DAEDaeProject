package ejbs;

import entities.Projetista;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ProjetistaBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email) throws MyEntityExistsException, MyConstraintViolationException {

        Projetista projetista = findProjetista(username);
        if (projetista != null) {
            throw new MyEntityExistsException("Projetista já registado!!!");
        }
        try{
            projetista = new Projetista(username, password, nome, email);
            manager.persist(projetista);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Projetista findProjetista(String username){
        return manager.find(Projetista.class, username);
    }

    public List<Projetista> getAllProjetistas(){
        return manager.createNamedQuery("getAllProjetistas", Projetista.class).getResultList();
    }
}
