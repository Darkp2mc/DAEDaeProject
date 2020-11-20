package ejbs;

import entities.Cliente;
import entities.Estrutura;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class EstruturaBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String nome, String tipoDeProduto,String dimensoes, String projetoNome)throws MyEntityExistsException, MyConstraintViolationException {
        Estrutura estrutura = findEstrutura(nome);
        if (estrutura != null){
            throw new MyEntityExistsException("Estrutura já registada!!!");
        }
        Projeto projeto = manager.find(Projeto.class,projetoNome);
        if (projeto == null){
            throw new MyEntityExistsException("Projeto inexistente!!!");
        }
        try{
            estrutura = new Estrutura(nome, tipoDeProduto,dimensoes, projeto);
            projeto.addEstrutura(estrutura);
            manager.persist(estrutura);
        }catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Estrutura findEstrutura(String name){
        return manager.find(Estrutura.class, name);
    }

    public List<Estrutura> getAllEstruturas(){
        return manager.createNamedQuery("getAllEstruturas", Estrutura.class).getResultList();
    }
}
