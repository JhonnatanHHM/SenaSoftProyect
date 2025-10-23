package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.TicketsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketsCrudRepository extends JpaRepository<TicketsEntity, Long> {

    @Query("SELECT t FROM TicketsEntity t WHERE t.usuario.idUsuario = :idUsuario")
    List<TicketsEntity> findByUsuarioIdUsuario(@Param("idUsuario") Long idUsuario);
}
