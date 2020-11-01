package entities;

import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class Pessoa {
    //Nome
    //Email
    @Version
    private int version;
    @Id
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;

    public Pessoa() {
    }

    public Pessoa(String username, @NotNull String password, @NotNull String name, @NotNull @Email String email) {
        this.username = username;
        this.password = password;
        this.name = name;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
