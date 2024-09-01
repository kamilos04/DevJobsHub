package com.kamiljach.devjobshub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamiljach.devjobshub.exceptions.UserNotFoundByJwtException;
import com.kamiljach.devjobshub.model.User;
import com.kamiljach.devjobshub.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtConfig jwtConfig;
    private ObjectMapper mapper;

    private UserRepository userRepository;

    public JwtAuthorizationFilter(JwtConfig jwtConfig, ObjectMapper mapper, UserRepository userRepository) {
        this.jwtConfig = jwtConfig;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Override
    protected  void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            String accessToken = jwtConfig.resolveToken(request);
            if(accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("token : "+accessToken);
            Claims claims = jwtConfig.resolveClaims(request);

            if(claims!=null && jwtConfig.validateClaims(claims)){
                String email = claims.getSubject();
                System.out.println("email : "+email);
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if(optionalUser.isEmpty()){
                    throw new UserNotFoundByJwtException();
                }
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, "", new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            errorDetails.put("message", "Authentication error");
            errorDetails.put("details",e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);
        }
    }


}
