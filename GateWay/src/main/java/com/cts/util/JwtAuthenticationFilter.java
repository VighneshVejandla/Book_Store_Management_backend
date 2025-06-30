package com.cts.util;

import com.cts.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Skip authentication for login endpoint
            if (request.getURI().getPath().contains("/user/authenticate") ||
                    request.getURI().getPath().contains("/user/adduser") ||
                    request.getURI().getPath().contains("/auth/register") ||
                    request.getURI().getPath().contains("/auth/login") ||
                    request.getURI().getPath().startsWith("/bookmanage/getRandombooks")||
                    request.getURI().getPath().startsWith("/bookmanage/viewbookbyid")||
                    request.getURI().getPath().startsWith("/reviews/trendingBooks"))  {
                return chain.filter(exchange);
            }

            // Extract JWT token from Authorization header
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String token = null;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                try {
                    username = jwtUtil.extractUsername(token);
                } catch (Exception e) {
                    return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            // Validate token
            if (username != null && jwtUtil.validateToken(token)) {
                List<String> roles = jwtUtil.extractRoles(token);

                // Check role-based access

                String path = request.getURI().getPath();
                System.out.println("Incomming Path => " + path);
                System.out.println("Roles : " + roles);

                if (!hasRequiredRole(request.getURI().getPath(), roles)) {
                    return onError(exchange, "Access denied: Insufficient privileges", HttpStatus.FORBIDDEN);
                }

                // Add user info to request headers for downstream services
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Id", username)
                        .header("X-User-Roles", String.join(",", roles))
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } else {
                return onError(exchange, "Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private boolean hasRequiredRole(String path, List<String> userRoles) {
        // Admin-only endpoints
        if (isAdminEndpoint(path)) {
            return userRoles.contains("ADMIN");
        }

        if (isUserEndpoint(path) || isCommonEndpoint(path)) {
            return userRoles.contains("ADMIN") || userRoles.contains("USER");
        }

        // User endpoints (both ADMIN and USER have access)
//        if (isUserEndpoint(path)) {
//            return userRoles.contains("USER") || userRoles.contains("ADMIN");
//        }

        // Default: allow access
        return true;
    }

    private boolean isAdminEndpoint(String path) {
        return path.contains("/bookmanage/addbook") ||
                path.contains("/bookmanage/updatebook") ||
                path.contains("/bookmanage/deletebook") ||
                path.contains("/authormanage/addnewauthor") ||
                path.contains("/authormanage/updateauthor") ||
                path.contains("/authormanage/deleteauthor") ||
                path.contains("/bookmanage/addnewcategory") ||
                path.contains("/bookmanage/updatecategory") ||
                path.contains("/bookmanage/deletecategory") ||
                path.contains("/inventory") ||
                path.contains("/user/viewallusers") ||
                path.contains("/user/deleteuser") ||
                path.contains("/user/updaterole") ||
                path.contains("/bookmanage/authorbybookid") ||
                path.contains("/bookstore/updateOrderById") ||
                path.contains("/bookstore/updateStatusById") ||
                path.contains("/reviews/hard/");
    }

    private boolean isUserEndpoint(String path) {
        return path.contains("/bookmanage/viewallbooks") ||
                path.contains("/bookmanage/viewbookbyid") ||
                path.contains("/bookmanage/viewbycategory") ||
                path.contains("/bookmanage/viewbyauthor") ||
                path.contains("/bookmanage/viewbytitle") ||
                path.contains("/authormanage/viewallauthors") ||
                path.contains("/authormanage/getauthorbyid") ||
                path.contains("/bookmanage/viewallcategories") ||
                path.contains("/bookmanage/getcategorybyid") ||
                path.startsWith("/api/v1/cart") ||
                path.contains("/bookstore/addOrder") ||
                path.contains("/bookstore/getOrderByUserId") ||
                path.contains("/bookstore/getOrderById") ||
                path.contains("/payments") ||
                path.contains("/reviews") ||
                path.contains("/profile") ||
                path.contains("/user/viewuserbyid") ||
                path.contains("/user/updateuser") ||
                path.contains("/user/changepassword");
    }

    private boolean isCommonEndpoint(String path) {
        return path.contains("/bookmanage/viewallbooks") ||
                path.contains("/bookmanage/viewbookbyid") ||
                path.contains("/bookmanage/viewbycategory") ||
                path.contains("/bookmanage/viewbyauthor") ||
                path.contains("/bookmanage/viewbytitle") ||
                path.contains("/authormanage/viewallauthors") ||
                path.contains("/authormanage/getauthorbyid") ||
                path.contains("/bookmanage/viewallcategories") ||
                path.contains("/bookmanage/getcategorybyid") ||
                path.contains("/authormanage/addnewauthor") ||
                path.contains("/authormanage/updateauthor") ||
                path.contains("/authormanage/deleteauthor") ||
                path.contains("/bookmanage/addnewcategory") ||
                path.contains("/bookmanage/updatecategory") ||
                path.contains("/bookmanage/deletecategory") ||
//                path.contains("/authorbybookid/{bookId}") ||
                path.contains("/profile");
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");

        String errorResponse = "{\"error\":\"" + err + "\",\"status\":" + httpStatus.value() + "}";

        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorResponse.getBytes())));
    }

    public static class Config {
        // Configuration properties if needed
    }
}