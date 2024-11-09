package com.example.testprojectforagencyamazon.security;

import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import com.example.testprojectforagencyamazon.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (isPublicEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        if (!StringUtils.startsWithIgnoreCase(authorization, AUTHORIZATION_HEADER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authorization.substring(AUTHORIZATION_HEADER_PREFIX.length()).trim();

            if (jwtService.isTokenExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtService.extractUsername(token);
            boolean refreshType = jwtService.isRefreshType(token);

            if (StringUtils.hasText(username) && !refreshType && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {
                authenticateUser(username);
            }

            if (refreshType) {
                throw new Exception("Expected an access token, but got a refresh token.");
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private boolean isPublicEndpoint(String requestURI) {
        return requestURI.equals("/register") || requestURI.equals("/login") || requestURI.equals("/refresh");
    }

    private void authenticateUser(String username) throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        log.error("Authentication error: {}", e.getMessage());
        ResponseContainer responseContainer = new ResponseContainer();
        responseContainer.setErrorMessageAndStatusCode(e.getMessage(), HttpStatus.BAD_REQUEST.value());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(responseContainer.getStatusCode());

        try (OutputStream responseStream = response.getOutputStream()) {
            MAPPER.writeValue(responseStream, responseContainer);
            responseStream.flush();
        }
    }
}


