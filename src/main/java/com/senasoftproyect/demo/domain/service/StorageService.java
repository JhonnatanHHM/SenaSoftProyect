package com.senasoftproyect.demo.domain.service;

import com.senasoftproyect.demo.domain.entitys.ImagenesEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    ImagenesEntity uploadImage(MultipartFile file) throws IOException;

    ImagenesEntity updateImage(Long id, MultipartFile file) throws IOException;

    void deleteImage(Long id) throws IOException;

    String getPresignedUrl(Long id) throws Exception;
}
