package com.kamiljach.devjobshub.config;

import com.kamiljach.devjobshub.exceptions.exceptions.JwtIsOnBlackListException;
import com.kamiljach.devjobshub.model.BlackListJwt;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.BlackListJwtRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class JwtConfig {


    private String secretKey = Constants.secretKey;
    private long expirationTime = Constants.expirationTime;

    private final String TOKEN_HEADER = Constants.header;
    private final String TOKEN_PREFIX = Constants.prefix;

    private JwtParser jwtParser;

    private Key key;

    private BlackListJwtRepository blackListJwtRepository;
    public JwtConfig(BlackListJwtRepository blackListJwtRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.blackListJwtRepository = blackListJwtRepository;
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

    public Claims resolveClaims(HttpServletRequest req) throws JwtIsOnBlackListException {
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

    public String resolveToken(HttpServletRequest req) throws JwtIsOnBlackListException {
        String bearerToken = req.getHeader(TOKEN_HEADER);
        ifJwtIsOnBlackListThrowException(bearerToken);
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

    public void ifJwtIsOnBlackListThrowException(String jwt) throws JwtIsOnBlackListException {
        Optional<BlackListJwt> optionalBlackListJwt = blackListJwtRepository.findByJwt(jwt);
        if(optionalBlackListJwt.isPresent()){throw new JwtIsOnBlackListException();}
    }

}
