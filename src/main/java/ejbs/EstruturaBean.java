package ejbs;

import entities.*;
import enums.AplicacaoPertendida;
import enums.TipoDeProduto;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;
import exceptions.MyEntityNotFoundException;
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

    public void create(String nome, String projetoNome, String tipoDeProduto, double numeroDeVaos,
                       double comprimentoDaVao, String aplicacao, double alturaDaLage, double sobrecarga)
            throws MyEntityExistsException, MyConstraintViolationException, MyIllegalArgumentException, MyEntityNotFoundException {

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
            (numeroDeVaos <= 0 || comprimentoDaVao <= 0  || aplicacao.isEmpty())){
            throw new MyIllegalArgumentException("Dimensões inválidas!!!");
        }
        else if(tipoDeProduto.equals("Lage") && alturaDaLage <= 0){
            throw new MyIllegalArgumentException("Dimensões inválidas!!!");
        }

        Estrutura estrutura = manager.find(Estrutura.class,nome);
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

    public Estrutura findEstrutura(String name) throws MyEntityNotFoundException {
        Estrutura estrutura = manager.find(Estrutura.class, name);
        if (estrutura != null){
            return estrutura;
        }

        throw new MyEntityNotFoundException("Estrutura com o nome " + name+ " nao existe!");

    }

    public List<Estrutura> getAllEstruturas(){
        return manager.createNamedQuery("getAllEstruturas", Estrutura.class).getResultList();
    }

    public void addVariante(String estruturaName,int varianteCodigo) throws MyEntityNotFoundException {
        Variante variante = manager.find(Variante.class,varianteCodigo);
        Estrutura estrutura = findEstrutura(estruturaName);
        if (!estrutura.getVariantes().contains(variante)) {
            estrutura.addVariante(variante);
            variante.addEstrutura(estrutura);
        }
    }

    public void rejeitar(String estruturaName) throws MyEntityNotFoundException {
        Estrutura estrutura = findEstrutura(estruturaName);

        estrutura.rejeitar();

    }

    public void aceitar(String estruturaName) throws MyEntityNotFoundException {
        Estrutura estrutura = findEstrutura(estruturaName);
        estrutura.aceitar();
    }

    public void removeVariante(String estruturaName, int varianteCodigo) throws MyEntityNotFoundException {
        Variante variante = manager.find(Variante.class,varianteCodigo);
        Estrutura estrutura = findEstrutura(estruturaName);

        estrutura.removeVariante(variante);
        variante.removeEstrutura(estrutura);
    }

}
