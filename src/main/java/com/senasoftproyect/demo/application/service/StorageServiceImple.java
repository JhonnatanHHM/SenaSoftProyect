package com.senasoftproyect.demo.application.service;

import com.senasoftproyect.demo.domain.entitys.ImagenesEntity;
import com.senasoftproyect.demo.domain.repository.ImagenesRespository;
import com.senasoftproyect.demo.domain.service.StorageDetailsService;
import com.senasoftproyect.demo.domain.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;

import java.util.UUID;

@Service
public class StorageServiceImple implements StorageService {

    private final StorageDetailsService storageDetailsService;
    private final ImagenesRespository imagenesRespository;

    @Autowired
    public StorageServiceImple(StorageDetailsService storageDetailsService, ImagenesRespository imagenesRespository) {
        this.storageDetailsService = storageDetailsService;
        this.imagenesRespository = imagenesRespository;
    }

    @Override
    public ImagenesEntity uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Archivo de imagen vacío o nulo");
        }

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String key = "imagenes/" + fileName;

        storageDetailsService.uploadImage(file, key);

        ImagenesEntity imagen = new ImagenesEntity();
        imagen.setNombreOriginal(file.getOriginalFilename());
        imagen.setKeyS3(key);
        return imagenesRespository.saveFile(imagen);
    }

    @Override
    public ImagenesEntity updateImage(Long id, MultipartFile file) throws IOException {
        ImagenesEntity imagen = imagenesRespository.findById(id)
                .orElseThrow(() -> new IOException("Imagen no encontrada"));

        storageDetailsService.deleteImage(imagen.getKeyS3());

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String newKey = "imagenes/" + fileName;

        storageDetailsService.uploadImage(file, newKey);

        imagen.setKeyS3(newKey);
        imagen.setNombreOriginal(file.getOriginalFilename());
        return imagenesRespository.saveFile(imagen);
    }

    @Override
    public void deleteImage(Long id) throws IOException {
        ImagenesEntity imagen = imagenesRespository.findById(id)
                .orElseThrow(() -> new IOException("Imagen no encontrada"));
        storageDetailsService.deleteImage(imagen.getKeyS3());
        imagenesRespository.deleteFileById(imagen.getId());
    }

    @Override
    public String getPresignedUrl(Long id) throws Exception {
        ImagenesEntity imagen = imagenesRespository.findById(id)
                .orElseThrow(() -> new Exception("Imagen no encontrada"));
        return storageDetailsService.generatePresignedUrl(imagen.getKeyS3(), Duration.ofMinutes(5));
    }
}
