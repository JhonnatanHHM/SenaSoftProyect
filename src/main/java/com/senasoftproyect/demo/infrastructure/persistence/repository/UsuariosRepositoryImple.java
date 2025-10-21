package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.UsuariosEntity;
import com.senasoftproyect.demo.domain.repository.UsuariosRepository;
import com.senasoftproyect.demo.domain.repository.crud.UsuariosCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuariosRepositoryImple implements UsuariosRepository {

    private UsuariosCrudRepository usuariosCrudRepository;

    @Autowired
    public UsuariosRepositoryImple(UsuariosCrudRepository usuariosCrudRepository) {
        this.usuariosCrudRepository = usuariosCrudRepository;
    }

    @Override
    public boolean existsById(Long idUsuario) {
        return usuariosCrudRepository.existsById(idUsuario);
    }

    @Override
    public Optional<UsuariosEntity> getByEmail(String email) {
        return usuariosCrudRepository.findByEmail(email);
    }

    @Override
    public Boolean getExistsByEmail(String email) {
        return usuariosCrudRepository.existsByEmail(email);
    }

    @Override
    public List<UsuariosEntity> getAll() {
        return usuariosCrudRepository.findAll();
    }


    @Override
    public void delete(Long idUsuario) {
        usuariosCrudRepository.deleteById(idUsuario);
    }

    @Override
    public UsuariosEntity save(UsuariosEntity usuario) {
        return usuariosCrudRepository.saveAndFlush(usuario);
    }

    @Override
    public Optional<UsuariosEntity> getByIdUsuario(Long idUsuario) {
        return usuariosCrudRepository.findById(idUsuario);
    }
}
