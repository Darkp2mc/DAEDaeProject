package ejbs;

import entities.Projetista;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ProjetistaBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email){
        //TODO
        Projetista projetista = new Projetista(username, password, nome, email);
        manager.persist(projetista);
    }

    public List<Projetista> getAllProjetistas(){
        return manager.createNamedQuery("getAllProjetistas",Projetista.class).getResultList();
    }

    public Projetista findProjetista(String username){
        return manager.find(Projetista.class,username);
    }






}
