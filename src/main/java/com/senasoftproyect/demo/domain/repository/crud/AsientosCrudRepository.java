package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.AsientosEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AsientosCrudRepository extends JpaRepository<AsientosEntity, Long> {
}
