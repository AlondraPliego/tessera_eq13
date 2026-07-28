package com.TESSERA.Eq13Tessera.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos extra que solo tienen los usuarios con rol EMPRESA.
 * Comparte la llave primaria con "usuario" (usuario_id).
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "nombre_empresa", nullable = false)
    private String nombreEmpresa;

    @Column(name = "rfc", nullable = false, unique = true)
    private String rfc;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "sitio_web")
    private String sitioWeb;

    public Empresa(Usuario usuario, String nombreEmpresa, String rfc, String telefono, String sitioWeb) {
        this.usuario = usuario;
        this.nombreEmpresa = nombreEmpresa;
        this.rfc = rfc;
        this.telefono = telefono;
        this.sitioWeb = sitioWeb;
    }
}
