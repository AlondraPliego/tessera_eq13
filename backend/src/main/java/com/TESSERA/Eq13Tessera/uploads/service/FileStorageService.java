package com.TESSERA.Eq13Tessera.uploads.service;

import com.TESSERA.Eq13Tessera.common.exception.ArchivoInvalidoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    // Carpeta REAL en el disco del VPS donde se guardan los archivos.
    // Ej: /home/tessera/uploads  (la creamos si no existe)
    @Value("${app.uploads.dir}")
    private String uploadsDir;

    // Prefijo con el que se sirven esos archivos por HTTP.
    // Ej: /uploads  ->  la imagen queda accesible en /uploads/flyers/xxxx.jpg
    @Value("${app.uploads.public-path}")
    private String publicPath;

    private static final List<String> EXTENSIONES_PERMITIDAS = List.of("jpg", "jpeg", "png", "webp");
    private static final long TAMANIO_MAXIMO_BYTES = 5L * 1024 * 1024; // 5 MB

    /**
     * Guarda el archivo dentro de {uploadsDir}/flyers con un nombre único
     * y regresa la URL pública relativa (ej: /uploads/flyers/9f3a-concierto.jpg).
     */
    public String guardarFlyer(MultipartFile archivo) {
        validar(archivo);

        String extension = obtenerExtension(archivo.getOriginalFilename());
        String nombreArchivo = UUID.randomUUID().toString() + "." + extension;

        try {
            Path carpetaFlyers = Paths.get(uploadsDir, "flyers");
            Files.createDirectories(carpetaFlyers);

            Path destino = carpetaFlyers.resolve(nombreArchivo).normalize();

            // Seguridad: nos aseguramos de que el archivo se quede DENTRO de la carpeta flyers
            if (!destino.startsWith(carpetaFlyers)) {
                throw new ArchivoInvalidoException("Nombre de archivo inválido");
            }

            archivo.transferTo(destino);

            return publicPath + "/flyers/" + nombreArchivo;
        } catch (IOException e) {
            throw new ArchivoInvalidoException("No se pudo guardar el archivo: " + e.getMessage());
        }
    }

    private void validar(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ArchivoInvalidoException("Debes enviar un archivo (campo 'file')");
        }
        if (archivo.getSize() > TAMANIO_MAXIMO_BYTES) {
            throw new ArchivoInvalidoException("El archivo supera el tamaño máximo permitido (5 MB)");
        }

        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ArchivoInvalidoException("El archivo debe ser una imagen (jpg, png o webp)");
        }

        String extension = obtenerExtension(archivo.getOriginalFilename());
        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new ArchivoInvalidoException("Extensión no permitida. Usa: " + EXTENSIONES_PERMITIDAS);
        }
    }

    private String obtenerExtension(String nombreOriginal) {
        if (!StringUtils.hasText(nombreOriginal) || !nombreOriginal.contains(".")) {
            throw new ArchivoInvalidoException("El archivo no tiene una extensión válida");
        }
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.') + 1).toLowerCase();
        return extension;
    }
}
