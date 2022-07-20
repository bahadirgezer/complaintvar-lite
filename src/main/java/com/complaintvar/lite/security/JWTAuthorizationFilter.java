package com.complaintvar.lite.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        log.info("Filtering request.");
        String header = request.getHeader(SecurityConstants.HEADER_STRING);
        log.debug(header);
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            log.debug("empty header");
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        log.info("getting authentication");
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        log.info(String.format("header token: %s", token));
        if (token != null) {
            DecodedJWT verify = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

            String username = verify.getSubject();
            //String role = verify.getClaim("role").asString();

            if (username != null) {
                log.info(String.format("decoded token signature: %s, username: %s", username, verify.getSignature()));
                //return new UsernamePasswordAuthenticationToken(username, null, getAuthorities(role));
                //return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                log.info("returning authentication");
                return new UsernamePasswordAuthenticationToken(username, "123478");
            }
            log.info("username is null");
            return null;
        }
        log.info("token is null");
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

}