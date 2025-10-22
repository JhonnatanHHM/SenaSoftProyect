package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.TicketsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketsCrudRepository extends JpaRepository<TicketsEntity, Long> {
}
