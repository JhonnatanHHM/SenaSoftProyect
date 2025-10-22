package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.AvionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvionesCrudRepository extends JpaRepository<AvionesEntity, Long> {
}
