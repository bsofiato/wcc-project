package com.matera.wcc.projeto.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoggedUserContextFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggedUserContextFilter.class);

    private static final String LOGGED_USER_KEY = "user";
    public static final String ANONYMOUS_USER = "<<anonymous>>";

    private String loggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            LOGGER.trace("Nenhum usuario logado identificado. Assumindo {} como identificador de usuario nos logs", ANONYMOUS_USER);
            return ANONYMOUS_USER;
        } else {
            LOGGER.trace("Assumindo {} como identificador de usuario nos logs", authentication.getName());
            return authentication.getName();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String loggedUser = this.loggedUser();
        try (MDC.MDCCloseable context = MDC.putCloseable(LOGGED_USER_KEY, loggedUser)) {
            LOGGER.debug("Incluindo usuario {} no contexto de logging", loggedUser);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }
}
