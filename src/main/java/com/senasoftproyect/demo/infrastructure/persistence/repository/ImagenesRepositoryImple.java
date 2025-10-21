package com.senasoftproyect.demo.infrastructure.persistence.repository;

import com.senasoftproyect.demo.domain.entitys.ImagenesEntity;
import com.senasoftproyect.demo.domain.repository.ImagenesRespository;
import com.senasoftproyect.demo.domain.repository.crud.ImagenesCrudRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ImagenesRepositoryImple implements ImagenesRespository {

    private ImagenesCrudRespository imagenesCrudRespository;

    @Autowired
    public ImagenesRepositoryImple(ImagenesCrudRespository storageCrudRepository) {
        this.imagenesCrudRespository = storageCrudRepository;
    }

    @Override
    public Optional<ImagenesEntity> findById(Long idArchivo) {
        return imagenesCrudRespository.findById(idArchivo);
    }

    @Override
    public ImagenesEntity saveFile(ImagenesEntity archivo) {
        return imagenesCrudRespository.save(archivo);
    }

    @Override
    public void deleteFileById(Long idArchivo) {
        imagenesCrudRespository.deleteById(idArchivo);
    }

}
