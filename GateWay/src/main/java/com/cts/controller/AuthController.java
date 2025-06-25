//package com.cts.controller;
//
//import com.cts.dto.ErrorDto;
//import com.cts.dto.LoginRequest;
//import com.cts.dto.RegisterDto;
//import com.cts.dto.UserDto;
//import com.cts.util.JwtUtil;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    @PostMapping("/login")
//    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody LoginRequest loginRequest) {
//        // Call user service to authenticate
//        return webClientBuilder.build()
//                .post()
//                .uri("http://localhost:8009/user/authenticate")
//                .bodyValue(loginRequest)
//                .retrieve()
//                .bodyToMono(UserDto.class)
//                .map(user -> {
//                    // Generate JWT token with roles
//                    List<String> roles = Arrays.asList(user.getRole().toUpperCase());
//                    String token = jwtUtil.generateToken(user.getEmail(), roles);
//
//                    Map<String, Object> response = new HashMap<>();
//                    response.put("token", token);
//                    response.put("user", user);
//                    response.put("roles", roles);
//
//                    return ResponseEntity.ok(response);
//                })
//                .onErrorReturn(ResponseEntity.badRequest().build());
//    }
//
//    @PostMapping("/validate")
//    public ResponseEntity<Map<String, Object>> validateToken(@Valid @RequestHeader("Authorization") String authHeader) {
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            if (jwtUtil.validateToken(token)) {
//                String username = jwtUtil.extractUsername(token);
//                List<String> roles = jwtUtil.extractRoles(token);
//
//                Map<String, Object> response = new HashMap<>();
//                response.put("valid", true);
//                response.put("username", username);
//                response.put("roles", roles);
//
//                return ResponseEntity.ok(response);
//            }
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("valid", false);
//        response.put("message", "Invalid token");
//
//        return ResponseEntity.badRequest().body(response);
//    }
//
//    @PostMapping("/register")
//    public Mono<ResponseEntity<Object>> register(@RequestBody RegisterDto registerDto) {
//        // Map RegisterDto → UserDto
//        UserDto userDto = new UserDto();
//        userDto.setUserId(registerDto.getUserId());
//        userDto.setName(registerDto.getName());
//        userDto.setEmail(registerDto.getEmail());
//        userDto.setPassword(registerDto.getPassword());
//        userDto.setRole("USER");
//
//        return webClientBuilder.build()
//                .post()
//                .uri("http://localhost:8009/user/adduser")
//                .bodyValue(userDto)
//                .retrieve()
//                .onStatus(
//                        status -> status.is4xxClientError() || status.is5xxServerError(),
//                        clientResponse -> clientResponse.bodyToMono(String.class)
//                                .flatMap(errorBody -> {
//                                    try {
//                                        // Parse the JSON error response
//                                        ObjectMapper objectMapper = new ObjectMapper();
//                                        JsonNode errorJson = objectMapper.readTree(errorBody);
//                                        String message = errorJson.get("message").asText();
//                                        return Mono.error(new RuntimeException(message));
//                                    } catch (Exception e) {
//                                        return Mono.error(new RuntimeException(errorBody));
//                                    }
//                                })
//                )
//                .bodyToMono(UserDto.class)
//                .map(user -> ResponseEntity.ok().body((Object) user))
//                .onErrorResume(error -> Mono.just(
//                        ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                                .body((Object) Map.of("error", error.getMessage()))
//                ));
//    }
//
//
//
////    @PostMapping("/register")
////    public Mono<ResponseEntity<Object>> register(@RequestBody RegisterDto registerDto) {
////        // Map RegisterDto → UserDto
////        UserDto userDto = new UserDto();
////        userDto.setUserId(registerDto.getUserId());
////        userDto.setName(registerDto.getName());
////        userDto.setEmail(registerDto.getEmail());
////        userDto.setPassword(registerDto.getPassword());
////        userDto.setRole("USER");
////
////        return webClientBuilder.build()
////                .post()
////                .uri("http://localhost:8009/user/adduser")
////                .bodyValue(userDto)
////                .retrieve()
////                .onStatus(HttpStatusCode::isError, clientResponse ->
////                        clientResponse.bodyToMono(String.class)
////                                .flatMap(errorBody -> {
////                                    try {
////                                        ObjectMapper mapper = new ObjectMapper();
////                                        JsonNode jsonNode = mapper.readTree(errorBody);
////
////                                        Map<String, Object> errorMap = new HashMap<>();
////                                        errorMap.put("status", clientResponse.statusCode().value());
////
////                                        // Extract fields if present
////                                        errorMap.put("message", jsonNode.has("message") ? jsonNode.get("message").asText() : "Unknown error");
////                                        errorMap.put("errorCode", jsonNode.has("errorCode") ? jsonNode.get("errorCode").asText() : "UNKNOWN");
////
////                                        return Mono.error(new RuntimeException(new ObjectMapper().writeValueAsString(errorMap)));
////                                    } catch (Exception e) {
////                                        return Mono.error(new RuntimeException("Error parsing response from user service: " + errorBody));
////                                    }
////                                })
////                )
////                .bodyToMono(UserDto.class)
////                .map(user -> ResponseEntity.ok().body((Object) user))
////                .onErrorResume(error -> {
////                    Map<String, Object> fallback = new HashMap<>();
////                    try {
////                        JsonNode node = new ObjectMapper().readTree(error.getMessage());
////                        fallback.put("message", node.has("message") ? node.get("message").asText() : "Unknown error");
////                        fallback.put("errorCode", node.has("errorCode") ? node.get("errorCode").asText() : "UNKNOWN");
////                        fallback.put("status", node.has("status") ? node.get("status").asInt() : 400);
////                    } catch (Exception e) {
////                        fallback.put("message", error.getMessage());
////                        fallback.put("errorCode", "PARSING_ERROR");
////                        fallback.put("status", 400);
////                    }
////                    return Mono.just(ResponseEntity.status((int) fallback.get("status")).body(fallback));
////                });
////    }
//
//}





package com.cts.controller;

import com.cts.dto.ErrorDto;
import com.cts.dto.LoginRequest;
import com.cts.dto.RegisterDto;
import com.cts.dto.UserDto;
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
                .uri("http://localhost:8009/user/authenticate")
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
                .bodyToMono(UserDto.class)
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
        userDto.setRole("USER");

        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8009/user/adduser")
                .bodyValue(userDto)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    // Create WebClientResponseException with the error body
//                                    WebClientResponseException webClientException = WebClientResponseException.create(
//                                            clientResponse.statusCode().value(),
//                                            clientResponse.statusCode().getReasonPhrase(),
//                                            clientResponse.headers().asHttpHeaders(),
//                                            errorBody.getBytes(),
//                                            null
//                                    );

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

    // Additional method to handle user operations that might need error handling
    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<Object>> getUserById(@PathVariable Long userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8009/user/getuser/{userId}", userId)
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

    @PutMapping("/user/{userId}")
    public Mono<ResponseEntity<Object>> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        return webClientBuilder.build()
                .put()
                .uri("http://localhost:8009/user/updateuser/{userId}", userId)
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

    @DeleteMapping("/user/{userId}")
    public Mono<ResponseEntity<Object>> deleteUser(@PathVariable Long userId) {
        return webClientBuilder.build()
                .delete()
                .uri("http://localhost:8009/user/deleteuser/{userId}", userId)
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
                .bodyToMono(String.class)
                .map(response -> {
                    Map<String, Object> successResponse = new HashMap<>();
                    successResponse.put("message", "User deleted successfully");
                    return ResponseEntity.ok().body((Object) successResponse);
                });
    }

    // Profile-related endpoints
    @PostMapping("/user/{userId}/profile")
    public Mono<ResponseEntity<Object>> createProfile(@PathVariable Long userId, @Valid @RequestBody Object profileDto) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:8009/user/{userId}/profile", userId)
                .bodyValue(profileDto)
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
                .bodyToMono(Object.class)
                .map(profile -> ResponseEntity.ok().body(profile));
    }

    @GetMapping("/user/{userId}/profile")
    public Mono<ResponseEntity<Object>> getProfile(@PathVariable Long userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8009/user/{userId}/profile", userId)
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
                .bodyToMono(Object.class)
                .map(profile -> ResponseEntity.ok().body(profile));
    }

    @PutMapping("/user/{userId}/profile")
    public Mono<ResponseEntity<Object>> updateProfile(@PathVariable Long userId, @Valid @RequestBody Object profileDto) {
        return webClientBuilder.build()
                .put()
                .uri("http://localhost:8009/user/{userId}/profile", userId)
                .bodyValue(profileDto)
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
                .bodyToMono(Object.class)
                .map(profile -> ResponseEntity.ok().body(profile));
    }

    @DeleteMapping("/user/{userId}/profile")
    public Mono<ResponseEntity<Object>> deleteProfile(@PathVariable Long userId) {
        return webClientBuilder.build()
                .delete()
                .uri("http://localhost:8009/user/{userId}/profile", userId)
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
                .bodyToMono(String.class)
                .map(response -> {
                    Map<String, Object> successResponse = new HashMap<>();
                    successResponse.put("message", "Profile deleted successfully");
                    return ResponseEntity.ok().body((Object) successResponse);
                });
    }
}