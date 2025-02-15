package com.ra.security.jwt;

import com.ra.security.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Component
public class JwtProvider {
    @Value("${SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${EXPIRED}")
    private String EXPIRED;
    private Logger logger = LoggerFactory.getLogger(JwtEntrypoint.class);
    public String generateToken(UserPrinciple userPrinciple) {
        // Tạo ra thời gian sống của token
        Instant now = Instant.now();
        Date expiryDate = Date.from(now.plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY)
                .setExpiration(expiryDate)
                .compact();

    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            logger.error("Invalid JWT: {}", ex.getMessage());
        }

        return false;
    }
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody().getSubject();
    }
}
