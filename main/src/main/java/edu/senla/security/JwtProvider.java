package edu.senla.security;

import ch.qos.logback.classic.Logger;
import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("$(jwt.secret)")
    private String jwtSecret;

    @Value("${jwt.sessionTime}")
    private long sessionTime;

    private final Logger LOG = (Logger) LoggerFactory.getLogger(JwtProvider.class);

    public String generateToken(String username) {
        Date expDate = Date.from(LocalDateTime.now().plusMinutes(sessionTime).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
