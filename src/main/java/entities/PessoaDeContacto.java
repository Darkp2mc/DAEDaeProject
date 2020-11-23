package entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
public class PessoaDeContacto extends  Pessoa{



    public PessoaDeContacto() {

    }

    public PessoaDeContacto(String username, @NotNull String password, @NotNull String nome, @NotNull @Email String email) {
        super(username, password, nome, email);

    }


}
