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

    @PostConstruct
    public void populateDB(){
        System.out.println("Creating cliente...");
        clienteBean.create("cliente_User","pass","Cliente","teste@teste.com","Rua");
        System.out.println("Finished!!!");
    }
}
