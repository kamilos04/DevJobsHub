package com.kamiljach.devjobshub.config;

import com.kamiljach.devjobshub.exceptions.exceptions.JwtIsBlockedException;
import com.kamiljach.devjobshub.model.Jwt;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.JwtRepository;
import com.kamiljach.devjobshub.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JwtConfig {

    private long expirationTime = Constants.expirationTime;

    private final String TOKEN_HEADER = Constants.header;
    private final String TOKEN_PREFIX = Constants.prefix;

    private JwtParser jwtParser;

    private Key key;

    private JwtService jwtService;


    public JwtConfig(@Value("${secret-key}") String secretKey, JwtService jwtService) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.jwtService = jwtService;

    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(Long.toString(user.getId()));
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(expirationTime));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) throws JwtIsBlockedException {
        try {
            String token = resolveToken(req);
            if(token != null) {
                jwtService.ifJwtIsBlockedThrowException(token);
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

    public String resolveToken(HttpServletRequest req) throws JwtIsBlockedException {
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

    public String getId(Claims claims) {
        return claims.getSubject();
    }



}
