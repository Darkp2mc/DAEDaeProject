package ejbs;

import entities.Fabricante;
import entities.Produto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ProdutoBean {

    @PersistenceContext
    EntityManager manager;

    public void create(String name, String tipo, String familia, String fabricanteUsername) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        Produto produto = manager.find(Produto.class, name);
        if (produto != null) {
            throw new MyEntityExistsException("Já existe um produto com o nome introduzido ("+ produto.getNome() +") !!!");
        }
        else{
            Fabricante fabricante = manager.find(Fabricante.class, fabricanteUsername);
            if (fabricante != null){
                try{
                    produto = new Produto(name, tipo, familia,fabricante);
                    fabricante.addProduto(produto);
                    manager.persist(produto);
                } catch (ConstraintViolationException e) {
                    throw new MyConstraintViolationException(e);
                }
            }
            else{
                throw new MyEntityNotFoundException("O username do fabricante inserido não existe!!!");
            }

        }
    }

    public Produto findCProduto(String name){
        return manager.find(Produto.class, name);
    }

    public List<Produto> getAllProdutos(){
        return manager.createNamedQuery("getAllProdutos", Produto.class).getResultList();
    }

}
