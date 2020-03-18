package com.matera.wcc.projeto.rest;

import org.apache.catalina.User;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class LoggedUserContextFilter extends OncePerRequestFilter {
    private static final String LOGGED_USER_KEY = "user";
    public static final String ANONYMOUS_USER = "<<anonymous>>";

    private Optional <String> loggedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map(Authentication::getName);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try (MDC.MDCCloseable context = MDC.putCloseable(LOGGED_USER_KEY, loggedUser().orElse(ANONYMOUS_USER))) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
