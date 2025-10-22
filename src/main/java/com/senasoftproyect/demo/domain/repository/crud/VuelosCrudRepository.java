package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.VuelosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VuelosCrudRepository extends JpaRepository<VuelosEntity, Long>{
}
