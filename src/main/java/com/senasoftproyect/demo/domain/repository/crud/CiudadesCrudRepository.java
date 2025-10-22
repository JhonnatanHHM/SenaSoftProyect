package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.CiudadesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadesCrudRepository extends JpaRepository<CiudadesEntity, Long> {
}
