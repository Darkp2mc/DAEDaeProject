package ejbs;

import entities.Produto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ProdutoBean {

    @PersistenceContext
    EntityManager em;

    public void create(String name){
        Produto p = new Produto(name);
        em.persist(p);
    }

}
