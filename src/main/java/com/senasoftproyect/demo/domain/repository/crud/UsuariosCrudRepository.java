package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosCrudRepository extends JpaRepository<UsuariosEntity, Long> {

    Optional<UsuariosEntity> findByEmail(String email);
    Boolean existsByEmail(String email);

}
