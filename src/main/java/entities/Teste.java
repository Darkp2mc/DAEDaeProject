package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TESTE")
public class Teste {

    @Version
    private int version;
    @Id
    protected String username;
    @NotNull
    protected String password;
    @NotNull
    protected String name;
    @NotNull
    @Email
    protected String email;

    public Teste() {
    }

    public Teste(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

}
