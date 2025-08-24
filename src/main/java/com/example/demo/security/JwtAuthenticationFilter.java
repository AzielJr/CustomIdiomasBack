package com.example.demo.security;

import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        String method = request.getMethod();
        
        System.out.println("[JWT DEBUG] Processing request: " + method + " " + requestPath);
        System.out.println("[JWT DEBUG] Headers: ");
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("[JWT DEBUG]   " + headerName + ": " + request.getHeader(headerName));
        }
        
        // Permitir endpoints de autenticação sem verificação de token
        if (requestPath.startsWith("/auth/login") || requestPath.startsWith("/auth/register")) {
            System.out.println("[JWT DEBUG] Skipping JWT validation for auth endpoint");
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader("Authorization");
        System.out.println("[JWT DEBUG] Authorization header: " + authHeader);
        final String jwt;
        final String userEmail;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            System.out.println("[JWT DEBUG] No valid Authorization header found, proceeding without authentication");
            filterChain.doFilter(request, response);
            return;
        }
        
        jwt = authHeader.substring(7);
        System.out.println("[JWT DEBUG] Extracted JWT token: " + jwt.substring(0, Math.min(20, jwt.length())) + "...");
        
        try {
            userEmail = jwtService.extractEmail(jwt);
            System.out.println("[JWT DEBUG] Email extracted from token: " + userEmail);
        }
        catch (ExpiredJwtException ex){
            System.out.println("[JWT DEBUG] Token expired: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expired\"}");
            return;
        }
        catch (Exception ex) {
            System.out.println("[JWT DEBUG] Invalid token: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid Token\"}");
            return;
        }
        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            System.out.println("[JWT DEBUG] Searching user in database: " + userEmail);
            var userEntity=usuarioRepository.findByEmail(userEmail).orElse(null);
            if(userEntity != null && jwtService.isTokenValid(jwt, userEntity.getEmail())) {
                System.out.println("[JWT DEBUG] User found and token valid: " + userEntity.getEmail());
                // Usar o role do usuário ou "USER" como padrão
                String role = userEntity.getRole() != null ? userEntity.getRole() : "USER";
                // Garantir que o role tenha o prefixo ROLE_
                if (!role.startsWith("ROLE_")) {
                    role = "ROLE_" + role;
                }
                System.out.println("[JWT DEBUG] User role: " + role);
                
                var authToken = new UsernamePasswordAuthenticationToken(
                        userEntity.getEmail(),
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("[JWT DEBUG] Authentication configured successfully for: " + userEntity.getEmail());
            } else {
                System.out.println("[JWT DEBUG] User not found or token invalid");
            }
        } else {
             System.out.println("[JWT DEBUG] No email extracted or authentication already exists");
         }
         
         System.out.println("[JWT DEBUG] Filter processing completed, continuing chain");
         filterChain.doFilter(request, response);
    }
}
