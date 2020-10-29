package ejbs;

import entities.Teste;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class TesteBean {

    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String name, String email) {

        Teste teste = new Teste(username, password, name, email);
        manager.persist(teste);

    }
}