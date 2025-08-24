package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repositorio;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository repositorio, PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() {
        return repositorio.findAll();
    }

    public Usuario findById(Long id) {
        return repositorio.findById(id).orElse(null);
    }

    public Usuario findByEmail(String email) {
        return repositorio.findByEmail(email).orElse(null);
    }

    public Usuario register(Usuario usuario) {
        // Criptografar a senha antes de salvar
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return repositorio.save(usuario);
    }

    public Usuario update(Usuario usuario) {
        // Se a senha foi alterada, criptografar
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            // Verificar se a senha já está criptografada (começa com $2a$, $2b$, etc.)
            if (!usuario.getSenha().startsWith("$2")) {
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            }
        }
        return repositorio.save(usuario);
    }

    public void deleteById(Long id) {
        repositorio.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return repositorio.findByEmail(email).isPresent();
    }

    public boolean existsByEmailAndNotId(String email, Long id) {
        return repositorio.findByEmail(email)
                .map(usuario -> !usuario.getId().equals(id))
                .orElse(false);
    }
}