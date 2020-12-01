package ejbs;

import entities.*;
import enums.AplicacaoPertendida;
import enums.TipoDeProduto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyIllegalArgumentException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class EstruturaBean {
    @PersistenceContext
    EntityManager manager;

    public void create(String nome, String projetoNome, String tipoDeProduto, String numeroDeVaos,
                       String comprimentoDaVao, String aplicacao, String alturaDaLage, String sobrecarga)
            throws MyEntityExistsException, MyConstraintViolationException, MyIllegalArgumentException {

        boolean flag = false;
        for(TipoDeProduto t : TipoDeProduto.values()){
            if (tipoDeProduto.equalsIgnoreCase(t.toString())){
                flag = true;
            }
        }

        if (!flag) throw new MyIllegalArgumentException("Tipo de produto inválido!!!");

        flag = false;
        for(AplicacaoPertendida a : AplicacaoPertendida.values()){
            if (aplicacao.equalsIgnoreCase(a.toString())){
                flag = true;
            }
        }

        if (!flag) throw new MyIllegalArgumentException("Aplicação inválida!!!");

        if ((tipoDeProduto.equals("Chapa") || tipoDeProduto.equals("Painel") || tipoDeProduto.equals("Perfil")) &&
            (numeroDeVaos.isEmpty() || comprimentoDaVao.isEmpty() || aplicacao.isEmpty())){
            throw new MyIllegalArgumentException("Dimensões inválidas!!!");
        }
        else if(tipoDeProduto.equals("Lage") && alturaDaLage.isEmpty()){
            throw new MyIllegalArgumentException("Dimensões inválidas!!!");
        }

        Estrutura estrutura = findEstrutura(nome);
        if (estrutura != null){
            throw new MyEntityExistsException("Estrutura já registada!!!");
        }
        Projeto projeto = manager.find(Projeto.class,projetoNome);
        if (projeto == null){
            throw new MyEntityExistsException("Projeto inexistente!!!");
        }
        try{
            estrutura = new Estrutura(nome, projeto, tipoDeProduto, numeroDeVaos,
                                    comprimentoDaVao, aplicacao, alturaDaLage, sobrecarga);
            projeto.addEstrutura(estrutura);
            manager.persist(estrutura);
        }catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

    public Estrutura findEstrutura(String name){
        return manager.find(Estrutura.class, name);
    }

    public List<Estrutura> getAllEstruturas(){
        return manager.createNamedQuery("getAllEstruturas", Estrutura.class).getResultList();
    }

    public void addVariante(String estruturaName,int varianteCodigo){
        Variante variante = manager.find(Variante.class,varianteCodigo);
        Estrutura estrutura = findEstrutura(estruturaName);
        if (!estrutura.getVariantes().contains(variante)) {
            estrutura.addVariante(variante);
            variante.addEstrutura(estrutura);
        }
    }
}
