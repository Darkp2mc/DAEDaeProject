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
import javax.persistence.LockModeType;
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

        if(p==null){
            throw new MyEntityNotFoundException("Projeto nao existe!!");
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

    public void updateProjeto(String nome, String projetistaUsername, String clienteUsername ) throws  MyEntityNotFoundException{

        Projeto projeto = manager.find(Projeto.class,nome);

        if(projeto==null){
            throw new MyEntityNotFoundException("Projeto nao existe!!");
        }

        Cliente cliente = manager.find(Cliente.class, clienteUsername);

        if (cliente==null){
            throw new MyEntityNotFoundException("Cliente com o nome" +clienteUsername+"nao existe");
        }

        Projetista projetista = manager.find(Projetista.class, projetistaUsername);

        if (projetista==null){
            throw new MyEntityNotFoundException("Projetista com o nome "+projetistaUsername+" não existe");
        }

        manager.lock(projeto,LockModeType.OPTIMISTIC);
        projeto.setCliente(cliente);
        projeto.setNome(nome);
        projeto.setProjetista(projetista);

    }




}
