package com.example.testprojectforagencyamazon.service;

import com.example.testprojectforagencyamazon.data.requests.RefreshRequest;
import com.example.testprojectforagencyamazon.data.requests.SignInRequest;
import com.example.testprojectforagencyamazon.data.response.JwtResponse;
import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@EnableCaching
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DbUserDetailsService userDetailsService;

    public ResponseContainer login(SignInRequest signInRequest) {
        ResponseContainer responseContainer = new ResponseContainer();

        Authentication authentication = authenticateUser(signInRequest.getUsername(), signInRequest.getPassword());
        if (authentication == null) {
            return responseContainer.setErrorMessageAndStatusCode("Authentication failed",
                    HttpStatus.BAD_REQUEST.value());
        }

        UserDetails userDetails = loadUserDetails(signInRequest.getUsername());
        if (userDetails == null) {
            return responseContainer.setErrorMessageAndStatusCode("User not found",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        String token = generateToken(userDetails);
        String refresh = generateRefreshToken(userDetails);

        responseContainer.setSuccessResult(new JwtResponse(token, refresh));
        return responseContainer;
    }

    public ResponseContainer refresh(RefreshRequest refreshRequest) {
        ResponseContainer responseContainer = new ResponseContainer();

        String refreshToken = refreshRequest.getRefresh();
        if (StringUtils.isEmpty(refreshToken)) {
            return handleError(responseContainer, "Refresh token is null",
                    HttpStatus.BAD_REQUEST.value());
        }

        if (jwtService.isTokenExpired(refreshToken)) {
            return handleError(responseContainer, "Refresh token expired",
                    HttpStatus.UNAUTHORIZED.value());
        }

        if (!jwtService.isRefreshType(refreshToken)) {
            return handleError(responseContainer, "Expected refresh token, but got access token",
                    HttpStatus.BAD_REQUEST.value());
        }

        String username = extractUsername(refreshToken);
        if (StringUtils.isEmpty(username)) {
            return handleError(responseContainer, "Username is null",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        UserDetails userDetails = loadUserDetails(username);
        if (userDetails == null) {
            return handleError(responseContainer, "User details not found",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        String access = generateToken(userDetails);
        boolean isNeedNewRefresh = !(jwtService.extractDuration(refreshToken).toHours() < 12);

        if (isNeedNewRefresh) {
            String newRefresh = generateRefreshToken(userDetails);
            responseContainer.setSuccessResult(new JwtResponse(access, newRefresh));
        } else {
            responseContainer.setSuccessResult(new JwtResponse(access, refreshToken));
        }

        return responseContainer;
    }

    private Authentication authenticateUser(String username, String password) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            logError("Authentication error: {}", e.getMessage());
            return null;
        }
    }

    private UserDetails loadUserDetails(String username) {
        try {
            return userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            logError("Error loading user details: {}", e.getMessage());
            return null;
        }
    }

    private String generateToken(UserDetails userDetails) {
        try {
            return jwtService.generateToken(userDetails);
        } catch (Exception e) {
            logError("Error generating token: {}", e.getMessage());
            return null;
        }
    }

    private String generateRefreshToken(UserDetails userDetails) {
        try {
            return jwtService.generateRefreshToken(userDetails);
        } catch (Exception e) {
            logError("Error generating refresh token: {}", e.getMessage());
            return null;
        }
    }

    private String extractUsername(String refreshToken) {
        try {
            return jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            logError("Error extracting username from refresh token: {}", e.getMessage());
            return null;
        }
    }

    private ResponseContainer handleError(ResponseContainer responseContainer, String errorMessage, int statusCode) {
        logError(errorMessage);
        return responseContainer.setErrorMessageAndStatusCode(errorMessage, statusCode);
    }

    private void logError(String message, Object... params) {
        log.error(message, params);
    }
}
