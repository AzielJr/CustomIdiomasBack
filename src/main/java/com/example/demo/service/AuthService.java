package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Usuario registrar(RegisterRequest request) {
        if(usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Usuário já Registrado !");
        }
        Usuario usuario = Usuario.builder().email(request.getEmail()).userName(request.getUserName()).senha(passwordEncoder.encode(request.getSenha())).ativo(true).build();
        return usuarioRepository.save(usuario);
    }
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow(()->new UsernameNotFoundException("Usuário não Encontrado !"));
        if(!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha Inválida !");
        }
        
        // Registrar o último login
        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);
        
        String token = jwtService.generateToken(usuario.getEmail());
        Date expiration = jwtService.extractExpiration(token);
        return new LoginResponse(token, usuario.getEmail(), usuario.getUserName(), jwtService.formatExpiration(expiration), usuario.getFoto());
    }
    public LoginResponse getData(String email, String token) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Usuário não Encontrado !"));
        Date expiration = jwtService.extractExpiration(token);
        return new LoginResponse(token, usuario.getEmail(), usuario.getUserName(), jwtService.formatExpiration(expiration), usuario.getFoto());
    }

    public Map<String, Object> debugUser(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Usuário não Encontrado !"));
        Map<String, Object> response = new HashMap<>();
        response.put("email", usuario.getEmail());
        response.put("userName", usuario.getUserName());
        response.put("role", usuario.getRole());
        response.put("ativo", usuario.getAtivo());
        response.put("senhaHash", usuario.getSenha());
        response.put("ultimoLogin", usuario.getUltimoLogin());
        return response;
    }

    public Map<String, Object> testPassword(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Usuário não Encontrado !"));
        boolean matches = passwordEncoder.matches(password, usuario.getSenha());
        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("passwordProvided", password);
        response.put("passwordMatches", matches);
        response.put("storedHash", usuario.getSenha());
        return response;
    }

}
