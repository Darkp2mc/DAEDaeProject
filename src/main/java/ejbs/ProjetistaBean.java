package ejbs;

import entities.Cliente;
import entities.Projetista;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.EJBException;
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

    public List<Projetista> getAllProjetistas(){
        return manager.createNamedQuery("getAllProjetistas",Projetista.class).getResultList();
    }

    public Projetista findProjetista(String username){
        return manager.find(Projetista.class,username);
    }


    public void removeProjeto(Projeto projeto) throws MyEntityNotFoundException{

        Projeto p = manager.find(Projeto.class,projeto.getNome());

        if(p== null){
            throw new MyEntityNotFoundException(" Projeto nao existe!");
        }

        Cliente cliente = p.getCliente();

        if (cliente==null){
            throw new MyEntityNotFoundException("Cliente com o nome" +cliente.getName()+"nao existe");
        }

        Projetista projetista = p.getProjetista();

        if (projetista==null){
            throw new MyEntityNotFoundException("Projetista com o nome "+projetista.getName()+" não existe");
        }

        projetista.removeProjeto(p);
        cliente.removeProjeto(p);
        manager.remove(p);

    }
}
