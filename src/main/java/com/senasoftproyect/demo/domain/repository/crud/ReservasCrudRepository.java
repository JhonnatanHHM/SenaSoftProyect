package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.ReservasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservasCrudRepository extends JpaRepository<ReservasEntity, Long> {
}
