package com.senasoftproyect.demo.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;

public interface StorageDetailsService {
    void uploadImage(MultipartFile file, String key) throws IOException;

    String generatePresignedUrl(String key, Duration duration);

    void deleteImage(String key);
}
