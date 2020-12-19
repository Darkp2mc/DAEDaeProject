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
        Projeto projeto = findProjeto(nome);
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



    public Projeto findProjeto(String nome){
        return manager.find(Projeto.class, nome);
    }

    public List<Projeto> getAllProjetos(){
        return manager.createNamedQuery("getAllProjetos", Projeto.class).getResultList();
    }

    public void rejeitar(String nome){
        Projeto projeto = findProjeto(nome);
        projeto.rejeitar();
    }

    public void terminar(String nome){
        Projeto projeto = findProjeto(nome);
        projeto.terminar();
    }


    public void aceitar(String nome) {
        Projeto projeto = findProjeto(nome);
        projeto.aceitar();
    }

    public void tornarVisivel(String nome) {
        Projeto projeto = findProjeto(nome);
        projeto.tornarVisivel();
    }

    public void removerEstrutura(Projeto projeto, Estrutura estrutura){
        projeto.removeEstrutura(estrutura);
    }
}
