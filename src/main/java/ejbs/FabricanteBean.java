package ejbs;

import entities.Fabricante;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class FabricanteBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email){
        //TODO
        Fabricante fabricante = new Fabricante(username, password, nome, email);
        manager.persist(fabricante);
    }
}
