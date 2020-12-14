package ejbs;

import entities.Variante;
import enums.AplicacaoPertendida;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton(name = "ConfigEJB")
@Startup
public class ConfigBean {

    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @EJB
    ClienteBean clienteBean;
    @EJB
    FabricanteBean fabricanteBean;
    @EJB
    ProjetistaBean projetistaBean;
    @EJB
    ProjetoBean projetoBean;
    @EJB
    ProdutoBean produtoBean;
    @EJB
    EstruturaBean estruturaBean;
    @EJB
    VarianteBean varianteBean;
    @EJB
    SimulacaoBean simulacaoBean;

    @EJB
    PessoaDeContactoBean pessoaDeContactoBean;

    @PostConstruct
    public void populateDB(){
        try {
            System.out.println("Creating pessoa de contacto");
            pessoaDeContactoBean.create("PC_User","pass","Alberto", "alberto@mail.com");
            System.out.println("Creating cliente...");
            clienteBean.create("cliente_User", "pass", "Cliente", "teste@teste.com", "Rua", "PC_User");
            clienteBean.create("cliente_User2", "pass", "Cliente", "teste@teste.com", "Rua", "PC_User");
            System.out.println("Creating cliente...");
            projetistaBean.create("projetista_User", "pass", "Projetista", "teste@teste.com");
            projetistaBean.create("projetista_User2", "pass", "Projetista", "teste@teste.com");
            System.out.println("Creating fabricante...");
            fabricanteBean.create("fabricante_User", "pass", "Fabricante", "teste@teste.com");
            System.out.println("Creating projeto...");
            projetoBean.create("Projeto1","cliente_User", "projetista_User");
            projetoBean.create("Projeto2","cliente_User", "projetista_User2");
            System.out.println("Finished!!!");

            System.out.println("####### A criar produtos...");
            produtoBean.create("Section C 220 BF","Perfil","C","fabricante_User");
            produtoBean.create("Section Z 220 BF","Perfil","Z","fabricante_User");
            System.out.println("####### A criar variantes...");

            //PODE LER-SE OS VALORES DOS PRODUTOS/VARIANTES DE EXCELS OU CSVs (ver excels fornecidos)
            //Exemplo básico de adição de variantes "à mão"
            varianteBean.create(1, "Section C 220 BF", "C 120/50/21 x 1.5", 13846, 13846, 375, 220000);
            varianteBean.create(2, "Section C 220 BF", "C 120/60/13 x 2.0", 18738, 18738, 500, 220000);

            //PODE LER-SE OS VALORES mcr_p E mcr_n A PARTIR DE UM EXCEL OU CSV (ver excels fornecidos para os produtos Perfil C e Z, que têm os valores mcr)
            //Exemplo básico de adição de valores mcr "à mão"
            Variante variante1 = varianteBean.getVariante(1);
            variante1.addMcr_p(3.0,243.2123113);
            variante1.addMcr_p(4.0,145.238784);
            variante1.addMcr_p(5.0,99.15039028);
            variante1.addMcr_p(6.0,73.71351699);
            variante1.addMcr_p(7.0,58.07716688);
            variante1.addMcr_p(8.0,47.68885195);
            variante1.addMcr_p(9.0,40.37070843);
            variante1.addMcr_p(10.0,34.9747033);
            variante1.addMcr_p(11.0,30.84866055);
            variante1.addMcr_p(12.0,27.59984422);

            //Válido para variantes simétricas, em que os mcr_p são iguais aos mcr_n
            variante1.setMcr_n((LinkedHashMap<Double, Double>) variante1.getMcr_p().clone());

            Variante variante2 = varianteBean.getVariante(2);
            variante2.addMcr_p(3.0,393.1408237);
            variante2.addMcr_p(4.0,241.9157907);
            variante2.addMcr_p(5.0,169.7815504);
            variante2.addMcr_p(6.0,129.3561949);
            variante2.addMcr_p(7.0,104.0782202);
            variante2.addMcr_p(8.0,86.9803928);
            variante2.addMcr_p(9.0,74.71876195);
            variante2.addMcr_p(10.0,65.52224563);
            variante2.addMcr_p(11.0,58.37786338);
            variante2.addMcr_p(12.0,52.65428332);

            //Válido para variantes de geometria simétrica, em que os mcr_p são iguais aos mcr_n
            variante2.setMcr_n((LinkedHashMap<Double, Double>) variante2.getMcr_p().clone());


            System.out.println("####### FINISHED!!");

            //EXEMPLO DA SIMULAÇÃO PARA DUAS VARIANTES DO PERFIL C, E PARA UMA ESTRUTURA DE 3 vãos (nb) de 3m cada (LVao) E SOBRECARGA 500000 (q)
            if(simulacaoBean.simulaVariante(3, 3.0, 500000, variante1)){
                System.out.println(variante1.getNome() + " pode ser usada.");
            }else{
                System.out.println(variante1.getNome() + " não pode ser usada.");
            }

            if(simulacaoBean.simulaVariante(3, 3.0, 500000, variante2)){
                System.out.println("A variante " + variante2.getNome() + " pode ser usada.");
            }else{
                System.out.println("A variante " + variante2.getNome() + " não pode ser usada.");
            }

            estruturaBean.create("Estrutura_teste2", "Projeto1", "Perfil", "3","3", "Cobertura", "1","1", 0);
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }

}
