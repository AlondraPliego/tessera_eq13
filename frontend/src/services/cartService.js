import api from "./api";


// Crea y paga la compra de una vez.
// detalles: [{ boletoEventoId, cantidad }]
export async function procesarPago(detalles) {
  const response = await api.post("/api/compras", { detalles });
  const compra = response.data; // CompraResponse: { id, clienteId, fecha, total, estado, detalles }

  return {
    folio: compra.id,
    total: compra.total,
  };
}

// Cancela una compra ya realizada (regresa el inventario)
export async function cancelarCompra(compraId) {
  const response = await api.patch(`/api/compras/${compraId}/cancelar`);
  return response.data;
}