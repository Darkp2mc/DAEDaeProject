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
public class ProdutoBean {

    @PersistenceContext
    EntityManager manager;

    public void create(String name, String tipo, String familia, double e, double n, double g, String fabricanteUsername) throws MyEntityExistsException, MyConstraintViolationException, MyEntityNotFoundException {
        Produto produto = manager.find(Produto.class, name);
        if (produto != null) {
            throw new MyEntityExistsException("Já existe um produto com o nome introduzido ("+ produto.getNome() +") !!!");
        }
        else{
            Fabricante fabricante = manager.find(Fabricante.class, fabricanteUsername);
            if (fabricante != null){
                try{
                    produto = new Produto(name, tipo, familia, e, n, g, fabricante);
                    if (!tipo.equals("Perfil")  && !tipo.equals("Chapa")  && !tipo.equals("Laje")  && !tipo.equals("Painel")){
                        throw new MyEntityExistsException("O Produto tem de ser do tipo 'Perfil', 'Chapa', 'Laje' ou 'Painel'");
                    }

                    if (!familia.equals("C")  && !familia.equals("Z")  && !familia.equals("Omega") && !familia.equals("Outro")){
                        throw new MyEntityExistsException("O Produto tem de ser da familia 'C', 'Z', 'Omega' ou 'Outro')");
                    }

                    if (e >= 0 && n >= 0){
                        produto.setG(round(e,n));
                    }else{
                        throw new MyEntityExistsException("Os valores das dimensoes 'e' e 'n' têm de ser diferentes de null e maiores que 0");
                    }

                    fabricante.addProduto(produto);
                    manager.persist(produto);
                } catch (ConstraintViolationException ce) {
                    throw new MyConstraintViolationException(ce);
                }
            }
            else{
                throw new MyEntityNotFoundException("O username do fabricante inserido não existe!!!");
            }

        }
    }

    public Produto findCProduto(String name){
        return manager.find(Produto.class, name);
    }

    public List<Produto> getAllProdutos(){
        return manager.createNamedQuery("getAllProdutos", Produto.class).getResultList();
    }

    public static double round(double e,double n) {
        double scale = Math.pow(10, 2);
        return Math.round((e/(2*(1+n))) * scale) / scale;
    }

    public void removeVariante(Variante variante) throws MyEntityNotFoundException {

        Variante v = manager.find(Variante.class,variante.getCodigo());

        if(v==null){
            throw new MyEntityNotFoundException("Variante nao existe!!");
        }


        Produto produto = v.getProduto();

        if (produto==null){
            throw new MyEntityNotFoundException("Produto com o nome "+produto.getNome()+" não existe");
        }

        produto.removeVariante(v);
        manager.remove(v);
    }

    public void updateVariante(int codigo, String nomeProduto, String name, double weff_p, double weff_n, double ar, double sigmaC, double h_mm, double b_mm, double c_mm, double t_mm, double a_mm, double p_kg_m, double yg_mm, double zg_mm, double ly_mm, double wy_mm, double lz_mm, double wz_mm, double ys_mm, double zs_mm, double lt_mm, double lw_mm) throws  MyEntityNotFoundException{

        Produto produto = manager.find(Produto.class,nomeProduto);

        if(produto == null){
            throw new MyEntityNotFoundException("Produto nao existe!!");
        }

        Variante variante = manager.find(Variante.class, codigo);

        if (variante == null){
            throw new MyEntityNotFoundException("Variante com o codigo" +variante.getCodigo()+"nao existe");
        }


        manager.lock(variante, LockModeType.OPTIMISTIC);
        variante.setCodigo(codigo);
        variante.setNome(name);
        variante.setWeff_p(weff_p);
        variante.setWeff_n(weff_n);
        variante.setAr(ar);
        variante.setSigmaC(sigmaC);
        variante.setH_mm(h_mm);
        variante.setB_mm(b_mm);
        variante.setC_mm(c_mm);
        variante.setT_mm(t_mm);
        variante.setA_mm(a_mm);
        variante.setP_kg_m(p_kg_m);
        variante.setYg_mm(yg_mm);
        variante.setZg_mm(zg_mm);
        variante.setLy_mm(ly_mm);
        variante.setWy_mm(wy_mm);
        variante.setLz_mm(lz_mm);
        variante.setWz_mm(wz_mm);
        variante.setYs_mm(ys_mm);
        variante.setZs_mm(zs_mm);
        variante.setLt_mm(lt_mm);
        variante.setLw_mm(lw_mm);


    }

}
