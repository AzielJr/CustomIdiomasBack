package com.example.demo.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKey key= Keys.hmacShaKeyFor("0ac3f731cae2a1aa164dfd0f529b4d40".getBytes(StandardCharsets.UTF_8));
    private static final SimpleDateFormat DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
    public String generateToken(String email) {
        long EXPIRATION_TIME=1000*60*60*24; // 24 horas
        return Jwts.builder().subject(email).expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME)).signWith(key).compact();
    }
    public String extractEmail(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean isTokenValid(String token, String email) {
        final String tokenEmail=extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration();
    }
    public String formatExpiration(Date expiration){
        return DATE_FORMAT.format(expiration);
    }
}
