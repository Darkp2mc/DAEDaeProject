package ejbs;

import entities.Estrutura;
import entities.Produto;
import entities.Variante;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class VarianteBean {

    @PersistenceContext
    EntityManager manager;

    public void create(int codigo, String nomeProduto, String name, double weff_p, double weff_n, double ar, double sigmaC) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        Variante variante = manager.find(Variante.class, codigo);
        if (variante != null) {
            throw new MyEntityExistsException("Já existe uma variante com o codigo introduzido ("+ variante.getCodigo() +") !!!");
        }
        else{
            Produto produto = manager.find(Produto.class, nomeProduto);
            if (produto != null){
                try{
                    variante = new Variante(codigo, produto, name, weff_p, weff_n, ar, sigmaC);
                    produto.addVariante(variante);
                    manager.persist(variante);
                } catch (ConstraintViolationException e) {
                    throw new MyConstraintViolationException(e);
                }
            }
            else{
                throw new MyEntityNotFoundException("O username do produto inserido não existe!!!");
            }
        }
    }

    public Variante getVariante(int codigo){
        return manager.find(Variante.class, codigo);
    }

    public List<Variante> getAllVariantes(){
        return manager.createNamedQuery("getAllVariantes", Variante.class).getResultList();
    }

    public List<Variante> getVariantesPorNomeDeProduto(String nomeProduto){
        List<Variante> variantes = getAllVariantes();
        variantes.removeIf(v -> !v.getProduto().getNome().equals(nomeProduto));

        return variantes;
    }


}
