package com.TESSERA.Eq13Tessera.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String email;

    @NotBlank(message = "El password es obligatorio")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial"
    )
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    private String rolNombre; // "EMPRESA" o "CLIENTE"

    // --- Campos que solo se usan si rolNombre = "CLIENTE" ---
    private String nombreUsuario;

    // --- Campos que solo se usan si rolNombre = "EMPRESA" ---
    private String nombreEmpresa;
    private String rfc;
    private String telefono;
    private String sitioWeb;
}