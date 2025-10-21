package com.senasoftproyect.demo.domain.repository;

import com.senasoftproyect.demo.domain.entitys.ImagenesEntity;

import java.util.Optional;

public interface ImagenesRespository {

    Optional<ImagenesEntity> findById (Long idArchivo);
    ImagenesEntity saveFile (ImagenesEntity archivo);
    void deleteFileById (Long idArchivo);

}
