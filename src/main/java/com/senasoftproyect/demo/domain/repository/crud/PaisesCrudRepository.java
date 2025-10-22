package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.PaisesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaisesCrudRepository extends JpaRepository<PaisesEntity, Long> {
}
