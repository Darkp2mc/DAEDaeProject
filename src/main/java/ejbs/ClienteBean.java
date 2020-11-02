package ejbs;

import entities.Cliente;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ClienteBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email, String morada ){
        //TODO
        Cliente cliente = new Cliente(username, password, nome, email, morada);
        manager.persist(cliente);
    }
}
