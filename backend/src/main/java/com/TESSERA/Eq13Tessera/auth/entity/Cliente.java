package com.TESSERA.Eq13Tessera.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos extra que solo tienen los usuarios con rol CLIENTE.
 * Comparte la llave primaria con "usuario" (usuario_id).
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    // @MapsId le dice a JPA: "el id de esta tabla ES el id del usuario asociado"
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "nombre_usuario", nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "telefono")
    private String telefono;

    public Cliente(Usuario usuario, String nombreUsuario) {
        this.usuario = usuario;
        this.nombreUsuario = nombreUsuario;
    }

    public Cliente(Usuario usuario, String nombreUsuario, String telefono) {
        this.usuario = usuario;
        this.nombreUsuario = nombreUsuario;
        this.telefono = telefono;
    }
}
