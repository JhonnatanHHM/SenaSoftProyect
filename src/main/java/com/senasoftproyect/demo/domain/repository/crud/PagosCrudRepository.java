package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.PagosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagosCrudRepository extends JpaRepository<PagosEntity, Long> {
}
