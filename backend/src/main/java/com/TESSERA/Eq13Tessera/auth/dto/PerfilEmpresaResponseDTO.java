package com.TESSERA.Eq13Tessera.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfilEmpresaResponseDTO {
    private String nombreEmpresa;
    private String rfc;
    private String correo;
    private String telefono;
    private String sitioWeb;
}