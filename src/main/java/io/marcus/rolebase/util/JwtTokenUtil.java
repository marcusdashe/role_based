package io.marcus.rolebase.util;


import io.jsonwebtoken.*;
import io.marcus.rolebase.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final long EXPIRE_DURATION = 24 * 60 * 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer("Rolebase")
                .claim("roles", user.getRoles().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.ES512, SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch(ExpiredJwtException e) {
            LOGGER.error("ExpiredJwtException: {}", e.getMessage());
        } catch(IllegalArgumentException ex) {
          LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch(MalformedJwtException ex){
            LOGGER.error("JWT is invalid", ex.getMessage());
        } catch(UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex.getMessage());
        } catch(SignatureException ex) {
            LOGGER.error("JWT signature verification failed", ex.getMessage());
        }
        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    Claims parseClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
