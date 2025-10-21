package com.senasoftproyect.demo.domain.repository.crud;

import com.senasoftproyect.demo.domain.entitys.ImagenesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenesCrudRespository extends JpaRepository<ImagenesEntity, Long> {
}
