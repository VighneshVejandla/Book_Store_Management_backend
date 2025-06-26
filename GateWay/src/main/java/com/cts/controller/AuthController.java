package com.cts.controller;

import com.cts.dto.*;
import com.cts.util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
        // Call user service to authenticate
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8006/user/authenticate")
                .bodyValue(loginRequest)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    // Create WebClientResponseException with the error body
                                    HttpStatusCode statusCode = clientResponse.statusCode();
                                    String reasonPhrase = statusCode instanceof HttpStatus
                                            ? ((HttpStatus) statusCode).getReasonPhrase()
                                            : statusCode.toString(); // Fallback if it's not an instance of HttpStatus

                                    WebClientResponseException webClientException = WebClientResponseException.create(
                                            statusCode.value(),
                                            reasonPhrase,
                                            clientResponse.headers().asHttpHeaders(),
                                            errorBody.getBytes(),
                                            null
                                    );

                                    return Mono.error(webClientException);
                                })
                )
                .bodyToMono(LoginResponseDto.class)
                .map(user -> {
                    // Generate JWT token with roles
                    List<String> roles = Arrays.asList(user.getRole().toUpperCase());
                    String token = jwtUtil.generateToken(user.getEmail(), roles);

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("user", user);
                    response.put("roles", roles);

                    return ResponseEntity.ok(response);
                });
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@Valid @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                List<String> roles = jwtUtil.extractRoles(token);

                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", username);
                response.put("roles", roles);

                return ResponseEntity.ok(response);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("valid", false);
        response.put("message", "Invalid token");

        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<Object>> register(@Valid @RequestBody RegisterDto registerDto) {
        // Map RegisterDto → UserDto
        UserDto userDto = new UserDto();
        userDto.setUserId(registerDto.getUserId());
        userDto.setName(registerDto.getName());
        userDto.setEmail(registerDto.getEmail());
        userDto.setPassword(registerDto.getPassword());
//        userDto.setRole("USER");

        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8006/user/adduser")
                .bodyValue(userDto)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {

                                    HttpStatusCode statusCode = clientResponse.statusCode();
                                    String reasonPhrase = statusCode instanceof HttpStatus
                                            ? ((HttpStatus) statusCode).getReasonPhrase()
                                            : statusCode.toString(); // Fallback if it's not an instance of HttpStatus

                                    WebClientResponseException webClientException = WebClientResponseException.create(
                                            statusCode.value(),
                                            reasonPhrase,
                                            clientResponse.headers().asHttpHeaders(),
                                            errorBody.getBytes(),
                                            null
                                    );

                                    return Mono.error(webClientException);
                                })
                )
                .bodyToMono(UserDto.class)
                .map(user -> ResponseEntity.ok().body((Object) user));
    }
}