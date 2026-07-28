package com.TESSERA.Eq13Tessera.uploads.controller;

import com.TESSERA.Eq13Tessera.uploads.dto.UploadResponse;
import com.TESSERA.Eq13Tessera.uploads.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private final FileStorageService fileStorageService;

    public UploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // POST /api/uploads/flyers  (multipart/form-data, campo "file")
    // Solo EMPRESA puede subir (ver SecurityConfig). Regresa la URL relativa
    // que luego se guarda tal cual en Evento.flyerPrincipal.
    @PostMapping(value = "/flyers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> subirFlyer(@RequestParam("file") MultipartFile file) {
        String url = fileStorageService.guardarFlyer(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UploadResponse(url));
    }
}
