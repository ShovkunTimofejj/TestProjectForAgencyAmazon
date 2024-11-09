package com.example.testprojectforagencyamazon.controller;

import com.example.testprojectforagencyamazon.data.UserDto;
import com.example.testprojectforagencyamazon.data.requests.RefreshRequest;
import com.example.testprojectforagencyamazon.data.requests.SignInRequest;
import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import com.example.testprojectforagencyamazon.service.AuthService;
import com.example.testprojectforagencyamazon.service.entityService.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "Welcome to Provisioning API!";
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseContainer> signIn(@Valid @RequestBody SignInRequest signInRequest){
        ResponseContainer responseContainer = authService.login(signInRequest);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }

    @PutMapping("/refresh")
    public ResponseEntity<ResponseContainer> refresh(@Valid @RequestBody RefreshRequest refreshRequest){
        ResponseContainer responseContainer = authService.refresh(refreshRequest);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseContainer> create(@RequestBody @Valid UserDto userDto){
        ResponseContainer responseContainer = userService.register(userDto);
        return ResponseEntity.status(responseContainer.getStatusCode()).body(responseContainer);
    }
}
