package ejbs;

import entities.Cliente;
import entities.Estrutura;
import entities.Projetista;
import entities.Projeto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class ProjetoBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String nome, String clienteUsername, String projetistaUsername) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        Projetista projetista = manager.find(Projetista.class,projetistaUsername);
        if (projetista == null){
            throw new MyEntityNotFoundException("Projetista nao encontrado");
        }

        Cliente cliente = manager.find(Cliente.class,clienteUsername);
        if (cliente == null) {
            throw new MyEntityNotFoundException("Cliente nao encontrado");
        }
        Projeto projeto = manager.find(Projeto.class, nome);
        if (projeto != null)
            throw new MyEntityExistsException("Já existe um projeto com o mesmo nome!!!");

        try {
            projeto = new Projeto(nome, cliente, projetista);
            manager.persist(projeto);
            cliente.addProjeto(projeto);
            projetista.addProjeto(projeto);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }



    public Projeto findProjeto(String nome) throws MyEntityNotFoundException {

        Projeto projeto = manager.find(Projeto.class, nome);
        if (projeto != null){
            return projeto;
        }

        throw new MyEntityNotFoundException("Projeto com o nome " + nome+ " nao existe!");

    }

    public List<Projeto> getAllProjetos(){
        return manager.createNamedQuery("getAllProjetos", Projeto.class).getResultList();
    }

    public void rejeitar(String nome) throws MyEntityNotFoundException {
        Projeto projeto = findProjeto(nome);
        projeto.rejeitar();
    }

    public void terminar(String nome) throws MyEntityNotFoundException {
        Projeto projeto = findProjeto(nome);
        for (Estrutura estrutura:projeto.getEstruturas()
             ) {
            estrutura.aceitar();
        }
        projeto.terminar();
    }


    public void aceitar(String nome) throws MyEntityNotFoundException {
        Projeto projeto = findProjeto(nome);
        projeto.aceitar();
    }

    public void tornarVisivel(String nome) throws MyEntityNotFoundException {
        Projeto projeto = findProjeto(nome);
        projeto.tornarVisivel();
    }

    public void setComentario(String nome, String comentario) throws MyEntityNotFoundException, MyConstraintViolationException {

        Projeto projeto = manager.find(Projeto.class,nome);
        if (projeto== null){
            throw new MyEntityNotFoundException("Projeto nao encontrado");
        }

        try{
            manager.lock(projeto, LockModeType.OPTIMISTIC);
            projeto.setComentario(comentario);
        }catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }


    }

    public void removerEstrutura(String projetoNome, String estruturaNome){
        Projeto projeto = manager.find(Projeto.class,projetoNome);
        Estrutura estrutura = manager.find(Estrutura.class,estruturaNome);
        projeto.removeEstrutura(estrutura);
    }
}
