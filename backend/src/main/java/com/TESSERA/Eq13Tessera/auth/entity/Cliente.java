package com.TESSERA.Eq13Tessera.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    // @MapsId sirve para decir que el id de esta tabla es el id del usuario asociado
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    public Cliente(Usuario usuario, String nombreUsuario, String telefono) {
        this.usuario = usuario;
        this.nombreUsuario = nombreUsuario;
        this.telefono = telefono;
    }
}