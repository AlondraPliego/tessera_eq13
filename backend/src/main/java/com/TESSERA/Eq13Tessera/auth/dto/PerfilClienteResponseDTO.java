package com.TESSERA.Eq13Tessera.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PerfilClienteResponseDTO {
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private LocalDate fechaNacimiento;
}