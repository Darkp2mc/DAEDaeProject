package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "PESSOAS")
public class Pessoa {

    @Version
    protected int version;
    @Id
    protected String username;
    @NotNull
    protected String password;
    @NotNull
    protected String nome;
    @NotNull
    @Email
    protected String email;

    public Pessoa() {
    }

    public Pessoa(String username, @NotNull String password, @NotNull String nome, @NotNull @Email String email) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
