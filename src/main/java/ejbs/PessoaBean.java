package ejbs;

import entities.Pessoa;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PessoaBean {

    @PersistenceContext
    EntityManager manager;

    public Pessoa authenticate(final String username, final String password) throws Exception {
        Pessoa pessoa = manager.find(Pessoa.class, username);
        if (pessoa != null && pessoa.getPassword().equals(Pessoa.hashPassword(password))) {
            return pessoa;
        }
        throw new Exception("Failed logging in with username '" + username + "': unknown username or wrong password");
    }
}
