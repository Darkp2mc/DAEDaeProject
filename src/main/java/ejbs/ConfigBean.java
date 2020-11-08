package ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
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

    @PostConstruct
    public void populateDB(){
        try {
            System.out.println("Creating cliente...");
            clienteBean.create("cliente_User", "pass", "Cliente", "teste@teste.com", "Rua");
            System.out.println("Creating cliente...");
            projetistaBean.create("projetista_User", "pass", "Projetista", "teste@teste.com");
            System.out.println("Creating fabricante...");
            fabricanteBean.create("fabricante_User", "pass", "Fabricante", "teste@teste.com");
            System.out.println("Creating projeto...");
            projetoBean.create("Projeto1","cliente_User", "projetista_User");
            System.out.println("Finished!!!");
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }
}
