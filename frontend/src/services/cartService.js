import api from "./api";

export async function procesarPago(detalles) {
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