package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.PasajerosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasajerosCrudRepository extends JpaRepository<PasajerosEntity, Long> {
}
