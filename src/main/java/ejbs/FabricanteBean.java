package ejbs;

import entities.*;
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
public class FabricanteBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String username, String password, String nome, String email) throws MyEntityExistsException, MyConstraintViolationException {

        Fabricante fabricante = manager.find(Fabricante.class, username);
        if (fabricante != null) {
            throw new MyEntityExistsException("Fabricante já registado!!!");
        }
        try{
            fabricante = new Fabricante(username, password, nome, email);
            manager.persist(fabricante);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Fabricante findFabricante(String username) throws MyEntityNotFoundException {
        Fabricante fabricante = manager.find(Fabricante.class, username);
        if (fabricante != null){
            return fabricante;
        }

        throw new MyEntityNotFoundException("Fabricante com o username " + username+ " nao existe!");

    }

    public List<Fabricante> getAllFabricantes(){
        return manager.createNamedQuery("getAllFabricantes", Fabricante.class).getResultList();
    }

    public void removeProduto(Produto produto) throws MyEntityNotFoundException {

        Produto p = manager.find(Produto.class,produto.getNome());

        if(p==null){
            throw new MyEntityNotFoundException("Produto nao existe!!");
        }

        Fabricante fabricante = p.getFabricante();

        if (fabricante==null){
            throw new MyEntityNotFoundException("Fabricante com o nome "+fabricante.getName()+" não existe");
        }

        fabricante.removeProduto(p);
        manager.remove(p);
    }

    //UPDATE produto
    public void updateProduto(String nome, String tipo, String familia, double e, double n, double g, String fabricanteUsername) throws  MyEntityNotFoundException{

        Produto produto = manager.find(Produto.class,nome);

        if(produto==null){
            throw new MyEntityNotFoundException("Produto nao existe!!");
        }

        Fabricante fabricante = manager.find(Fabricante.class, fabricanteUsername);

        if (fabricante==null){
            throw new MyEntityNotFoundException("Fabricante com o nome" +fabricante.getName()+"nao existe");
        }

        manager.lock(produto, LockModeType.OPTIMISTIC);
        if (nome == null || tipo == null || familia == null ||  e < 0 || n < 0){
            throw new MyEntityNotFoundException("Todos os atributos do produto têm de ser preenchidos");
        }
        if (!tipo.equals("Chapa") && !tipo.equals("Laje") && !tipo.equals("Perfil") && !tipo.equals("Painel")){
            throw new MyEntityNotFoundException("O Produto tem de ser do tipo 'Perfil', 'Chapa', 'Laje' ou 'Painel'");
        }

        if (!familia.equals("C")  && !familia.equals("Z")  && !familia.equals("Omega") && !familia.equals("Outro") ){
            throw new MyEntityNotFoundException("O Produto tem de ser da familia 'C', 'Z','Omega' ou 'Outro')");
        }



        produto.setNome(nome);
        produto.setTipo(tipo);
        produto.setFamilia(familia);
        produto.setE(e);
        produto.setN(n);
        produto.setG((e/(2*(1-n))));



    }

    public void removeVariante(Produto produto, Variante variante) throws MyEntityNotFoundException {

        Produto p = manager.find(Produto.class,produto.getNome());

        if(p==null){
            throw new MyEntityNotFoundException("Produto nao existe!!");
        }

        Fabricante fabricante = p.getFabricante();

        if (fabricante==null){
            throw new MyEntityNotFoundException("Fabricante com o nome "+fabricante.getName()+" não existe");
        }

        Variante v = p.getVariantes().get(variante.getCodigo());

        if (v == null){
            throw new MyEntityNotFoundException("Variante com o codigo "+variante.getCodigo()+" não existe");
        }

        produto.removeVariante(v);
        manager.remove(v);
    }

}
