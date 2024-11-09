package com.example.testprojectforagencyamazon.security;

import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthorizationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ResponseContainer responseContainer = new ResponseContainer();
        responseContainer.setErrorMessageAndStatusCode(authException.getMessage(), HttpStatus.BAD_REQUEST.value());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(responseContainer.getStatusCode());

        try (var responseStream = response.getOutputStream()) {
            MAPPER.writeValue(responseStream, responseContainer);
            responseStream.flush();
        }
    }
}

