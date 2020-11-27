package ejbs;

import entities.Document;
import entities.Projeto;
import entities.Pessoa;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class DocumentBean {

    @PersistenceContext
    EntityManager manager;

    public void create(String nome, String filepath, String filename) throws MyEntityNotFoundException, MyEntityExistsException,
            MyConstraintViolationException {

        Projeto projeto = manager.find(Projeto.class,nome);
        if(projeto== null){
            throw  new MyEntityNotFoundException("Projeto com "+nome+ "nao existe");
        }

        try{
            Document d = new Document(filepath,filename,projeto);
            projeto.addDocument(d);
            manager.persist(d);
        }
        catch (ConstraintViolationException e){
            throw new MyConstraintViolationException(e);
        }


    }

    public Document findDocument(int id){
        return manager.find(Document.class,id);
    }

    public List<Document> getProjectDocuments(String nome){
        return manager.createNamedQuery("getProjectDocuments", Document.class).getResultList();
    }

}
