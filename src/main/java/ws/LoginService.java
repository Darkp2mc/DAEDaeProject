package ws;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import dtos.AuthDTO;
import ejbs.PessoaBean;
import entities.Pessoa;
import jwt.Jwt;
import ejbs.JwtBean;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.logging.Logger;

@Path("/login")
public class LoginService {
    private static final Logger log = Logger.getLogger(LoginService.class.getName());
    @EJB
    JwtBean jwtBean;
    @EJB
    PessoaBean pessoaBean;

    @POST
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(AuthDTO authDTO) {
        try {
            Pessoa pessoa = pessoaBean.authenticate(authDTO.getUsername(), authDTO.getPassword());
            if (pessoa != null) {
                if (pessoa.getUsername() != null) {
                    log.info("Generating JWT for user " + pessoa.getUsername());
                }
                String token = jwtBean.createJwt(pessoa.getUsername(), new String[]{pessoa.getClass().getSimpleName()});
                return Response.ok(new Jwt(token)).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/claims")
    public Response demonstrateClaims(@HeaderParam("Authorization") String auth) {

        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                JWT j = JWTParser.parse(auth.substring(7));
                return Response.ok(j.getJWTClaimsSet().getClaims()).build();
            } catch (ParseException e) {
                log.warning(e.toString());
                return Response.status(400).build();
            }
        } return Response.status(204).build();
    }
}
