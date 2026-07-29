import api from "./api";

// `items` viene de SeleccionAsientos.jsx / Carrito.jsx con la forma
// { reservaId, boletoEventoId, nombre, cantidad, precio, expiraEn }.
// El DTO real del backend (DetalleCompraDTO) solo acepta boletoEventoId,
// cantidad, reservaId y subtotal — si mandamos los demás campos, Jackson
// rechaza la petición por propiedades desconocidas.
export async function procesarPago(items) {
  const detalles = items.map((item) => ({
    boletoEventoId: item.boletoEventoId,
    cantidad: item.cantidad,
    reservaId: item.reservaId,
  }));

  const response = await api.post("/api/compras", { detalles });
  const compra = response.data;

  return {
    folio: compra.id,
    total: compra.total,
  };
}


export async function cancelarCompra(compraId) {
  const response = await api.patch(`/api/compras/${compraId}/cancelar`);
  return response.data;
}


export async function crearReserva(datosReserva) {
  const response = await api.post("/api/reservas", datosReserva);
  return response.data;
}

export async function liberarReserva(reservaId) {
  const response = await api.delete(`/api/reservas/${reservaId}`);
  return response.data;
}