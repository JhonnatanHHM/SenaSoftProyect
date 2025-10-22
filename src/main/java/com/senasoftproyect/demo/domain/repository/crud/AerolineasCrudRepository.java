package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.AerolineasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AerolineasCrudRepository extends JpaRepository<AerolineasEntity, Long> {
}
