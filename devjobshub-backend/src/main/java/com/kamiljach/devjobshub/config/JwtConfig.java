package com.kamiljach.devjobshub.config;

import com.kamiljach.devjobshub.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtConfig {


    private String secretKey = JwtConstants.secretKey;
    private long expirationTime = JwtConstants.expirationTime;

    private final String TOKEN_HEADER = JwtConstants.header;
    private final String TOKEN_PREFIX = JwtConstants.prefix;

    private JwtParser jwtParser;

    private Key key;
    public JwtConfig() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        System.out.println(secretKey);
        System.out.println(expirationTime);
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(expirationTime));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if(token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException e) {
            req.setAttribute("expired", e.getMessage());
            throw e;
        } catch (Exception e) {
            req.setAttribute("invalid", e.getMessage());
            throw e;
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(TOKEN_HEADER);
        if(bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

}
