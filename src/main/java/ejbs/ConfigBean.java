package ejbs;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton(name = "ConfigEJB")
@Startup
public class ConfigBean {

    //@EJB
    //TesteBean testeBean;

    @PostConstruct
    public void populateDB(){
        //System.out.println("Creating teste...");
        //testeBean.create("teste","pass","name","teste@teste.com");
        //System.out.println("Finished!!!");
    }
}
