package ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton(name = "ConfigEJB")
@Startup
public class ConfigBean {

    @EJB
    ClienteBean clienteBean;
    @EJB
    FabricanteBean fabricanteBean;
    @EJB
    ProjetistaBean projetistaBean;

    @PostConstruct
    public void populateDB(){
        System.out.println("Creating cliente...");
        clienteBean.create("cliente_User","pass","Cliente","teste@teste.com","Rua");
        System.out.println("Creating projetista...");
        projetistaBean.create("projetista_User","pass","Projetista","teste@teste.com");
        System.out.println("Creating fabricante...");
        fabricanteBean.create("fabricante_User","pass","Fabricante","teste@teste.com");
        System.out.println("Finished!!!");

    }
}
