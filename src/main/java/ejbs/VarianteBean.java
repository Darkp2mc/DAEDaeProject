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

    public void create(int codigo, String nomeProduto, String name, double weff_p, double weff_n,
                       double ar, double sigmaC, double h_mm, double b_mm, double c_mm, double t_mm,
                       double a_mm, double p_kg_m, double yg_mm, double zg_mm, double ly_mm,
                       double wy_mm, double lz_mm, double wz_mm, double ys_mm, double zs_mm,
                       double lt_mm, double lw_mm) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        Variante variante = manager.find(Variante.class, codigo);
        if (variante != null) {
            throw new MyEntityExistsException("Já existe uma variante com o codigo introduzido ("+ variante.getCodigo() +") !!!");
        }
        else{
            Produto produto = manager.find(Produto.class, nomeProduto);
            if (produto != null){
                try{
                    variante = new Variante(codigo, produto, name, weff_p, weff_n, ar, sigmaC, h_mm, b_mm, c_mm, t_mm, a_mm, p_kg_m, yg_mm, zg_mm, ly_mm, wy_mm, lz_mm, wz_mm, ys_mm, zs_mm, lt_mm, lw_mm);
                    if (h_mm != 0 && b_mm != 0 && c_mm != 0 && t_mm != 0 && a_mm != 0 && p_kg_m != 0 && yg_mm != 0 && zg_mm != 0 && ly_mm != 0 && wy_mm != 0 && lz_mm != 0 && wz_mm != 0 && ys_mm != 0 && zs_mm != 0 && lt_mm != 0 && lw_mm != 0){

                    }else{
                        throw new MyEntityExistsException("Os valores das propriedades da variante têm de ser preenchidas");
                    }
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
