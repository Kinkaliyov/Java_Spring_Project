package com.library.dea.controller;

import com.library.dea.dto.AuthResponse;
import com.library.dea.dto.LoginRequest;
import com.library.dea.dto.RegisterForm;
import com.library.dea.service.JwtService;
import com.library.dea.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "Auth operations")
public class AuthRestController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthRestController(UserService userService,
                              AuthenticationManager authenticationManager,
                              JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered!"),
            @ApiResponse(responseCode = "400", description = "Validation error!"),
            @ApiResponse(responseCode = "409", description = "User already exists!")
    })
    public ResponseEntity<String> register(@Valid @RequestBody RegisterForm form) {
        userService.register(form);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    @Operation(summary = "Login API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful!"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password!")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String token = jwtService.generateToken(loginRequest.getUsername());

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password!");
        }
    }
}