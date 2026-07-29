package com.TESSERA.Eq13Tessera.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

// Lo que necesita el frontend (MisBoletos.jsx) para pintar "mis boletos":
// evento, sección, fecha, etc. Se arma cruzando compra -> detalle_compra ->
// boleto_evento -> evento / zona / fecha_evento.
//
// NOTA IMPORTANTE: este sistema no guarda un "asiento" individual en la base
// de datos (la asignación de asiento puntual vive en seatmap.pro y se resuelve
// vía la reserva, que se borra al confirmarse la compra). Por eso este DTO no
// trae un campo "asiento": lo más específico que podemos ofrecer hoy es la
// sección/zona comprada y la cantidad de boletos de esa compra.
@Data
@AllArgsConstructor
public class MiBoletoResponse {
    private Long detalleCompraId;
    private Long compraId;
    private String estadoCompra;      // PAGADA, CANCELADA
    private Long eventoId;
    private String evento;
    private String descripcionEvento;
    private String seccion;           // nombre de la zona comprada
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private LocalDate fecha;          // próxima función del evento (o null si no tiene fechas)
    private LocalTime hora;
    private String ciudad;
    private String flyerPrincipal;
}
