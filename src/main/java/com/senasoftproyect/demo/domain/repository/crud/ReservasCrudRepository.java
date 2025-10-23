package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.ReservasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservasCrudRepository extends JpaRepository<ReservasEntity, Long> {

}
