package com.TESSERA.Eq13Tessera.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {

    @NotBlank(message = "El token es obligatorio")
    private String token;

    @NotBlank(message = "El password es obligatorio")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial"
    )
    private String nuevoPassword;
}