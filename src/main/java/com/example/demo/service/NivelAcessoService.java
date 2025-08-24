package com.example.demo.service;

import com.example.demo.model.NivelAcesso;
import com.example.demo.repository.NivelAcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    public class NivelAcessoService {
        private final NivelAcessoRepository repositorio;

        @Autowired
        public NivelAcessoService(NivelAcessoRepository repositorio) {
            this.repositorio = repositorio;
        }

        public List<NivelAcesso> findAll(){
            return repositorio.findAll();
        }

        public NivelAcesso findById(Long id){
            return repositorio.findById(id).orElse(null);
        }

        public NivelAcesso register(NivelAcesso t) {
            return repositorio.save(t);
        }

        public void deleteById(Long id) {
            repositorio.deleteById(id);
        }
    }


