package com.TESSERA.Eq13Tessera.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos extra que solo tienen los usuarios con rol ADMIN.
 * Comparte la llave primaria con "usuario" (usuario_id).
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "administradores")
public class Administrador {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "nivel_acceso", nullable = false)
    private String nivelAcceso;

    public Administrador(Usuario usuario, String nivelAcceso) {
        this.usuario = usuario;
        this.nivelAcceso = nivelAcceso;
    }
}
