package com.sistema.blog.security;




import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.sistema.blog.exceptions.BlogAppExceptions;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.Jwts.SIG;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret = "defaultSecretasdasiug872b3uiybvsu78y2b3iuybasqwdqwd2dwuh2'h293`hd293db32dhn2iudbi/@7ñ/2dbp29udb`'29biubfwebfuywebfuywebvoufywveyf@@ññ!!!D@an13lqwd´wqd";    

    
    @Value("${app.jwt-expiration-in-ms}")
    private int jwtExpirationInMs;

    
    private final SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    
    
    public String generarToken(Authentication authentication){
        String username = authentication.getName();


        Date fechaActual = new Date();

        Date fechaExpiracion = new Date(fechaActual.getTime() + jwtExpirationInMs);

        try {
            String token = Jwts.builder()
                            .subject(username)
                            .issuedAt(new Date())
                            .expiration(fechaExpiracion)
                            .signWith(secretKey, SIG.HS512)
                            .compact();
        
            return token;
        } catch (Exception e) {
            
            e.printStackTrace();
            throw new BlogAppExceptions(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar el token");
        }
    }


    
    public String obtenerUsernameDelToken(String token){

        
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

        return claims.getSubject();        
    }


    
    public boolean validarToken(String token){
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
            
        } catch (SignatureException ex) {
            throw new BlogAppExceptions(HttpStatus.BAD_REQUEST, "Firma JWT no valida");

        } catch(MalformedJwtException ex){
            throw new BlogAppExceptions(HttpStatus.BAD_REQUEST, "Token JWT mal formado");

        } catch(ExpiredJwtException ex){
            throw new BlogAppExceptions(HttpStatus.BAD_REQUEST, "Token JWT expirado");

        } catch(UnsupportedJwtException ex){
            throw new BlogAppExceptions(HttpStatus.BAD_REQUEST, "Token JWT no compatbile");

        } catch(IllegalArgumentException ex){
            throw new BlogAppExceptions(HttpStatus.BAD_REQUEST, "JWT vacio");
        }
    }

}
