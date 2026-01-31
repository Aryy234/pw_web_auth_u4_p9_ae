package uce.edu.web.api.interfaces;

import java.time.Instant;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

public class AuthResource {

       @GET
    @Path("/token")
    public TokenResponse token(
            @QueryParam("user") @DefaultValue("estudiante1") String user,
            @QueryParam("password") @DefaultValue("password") String password
    )
    
    //Donde se compara el password recibido con el password almacenado en la base de datos
        // Lógica de autenticación (verificar usuario y contraseña)
        // Aquí se asume que la autenticación es exitosa para simplificar
        
    
    {
        String issuer = "matricula-auth";
        long ttl = 3600;
 
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttl);
 
        String jwt = Jwt.issuer(issuer)
                .subject(user)
                .groups(Set.of(role))     // roles: user / admin
                .issuedAt(now)
                .expiresAt(exp)
                .sign();
 
        return new TokenResponse(jwt, exp.getEpochSecond(), role);
    }

       public static class TokenResponse {
        public String accessToken;
        public long expiresAt;
        public String role;
 
        public TokenResponse() {}
        public TokenResponse(String accessToken, long expiresAt, String role) {
            this.accessToken = accessToken;
            this.expiresAt = expiresAt;
            this.role = role;
        }
    }

}
