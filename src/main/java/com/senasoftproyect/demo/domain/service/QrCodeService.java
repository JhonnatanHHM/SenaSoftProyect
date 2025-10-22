package com.senasoftproyect.demo.domain.service;

public interface QrCodeService {
    String generateQrCodeBase64(String data, int width, int height);
}
