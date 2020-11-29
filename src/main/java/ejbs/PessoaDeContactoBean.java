package ejbs;

import entities.PessoaDeContacto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless
public class PessoaDeContactoBean {

    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email)
    throws MyEntityExistsException, MyConstraintViolationException {

        PessoaDeContacto pessoaDeContacto = findPessoaContacto(username);

        if(pessoaDeContacto != null){
            throw new MyEntityExistsException("Pessoa de contacto já registado!");
        }

        try{
            pessoaDeContacto= new PessoaDeContacto(username,password,nome,email);
            manager.persist(pessoaDeContacto);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }



    }

    private PessoaDeContacto findPessoaContacto(String username) {
        return manager.find(PessoaDeContacto.class,username);
    }


}
