package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.AsientosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsientosCrudRepository extends JpaRepository<AsientosEntity, Long> {
}
