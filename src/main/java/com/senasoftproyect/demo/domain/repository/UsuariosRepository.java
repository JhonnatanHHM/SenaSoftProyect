package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.UsuariosEntity;

import java.util.List;
import java.util.Optional;

public interface UsuariosRepository {

    boolean existsById (Long idUsuario);

    Optional<UsuariosEntity> getByEmail(String email);

    Boolean getExistsByEmail(String email);

    List<UsuariosEntity> getAll();

    void delete(Long idUsuario);

    UsuariosEntity save(UsuariosEntity usuario);

    Optional<UsuariosEntity> getByIdUsuario(Long idUsuario);
}
