import api from "./api";

// Trae los boletos que el usuario tiene en el carrito
export async function getCarrito() {
  // const response = await api.get("/api/carrito");
  // return response.data;

  // --- Placeholder mientras conectamos el backend real ---
  return {
    tiempoExpiraSegundos: 5 * 60, // 5 minutos de reserva
    cargoServicio: 100.0,
    items: [
      { id: 1, evento: "HUMBE", tipo: "Tour", seccion: "GCE-015", asiento: "F20", fecha: "13 ago, 21:00", precio: 5500.0, imagenUrl: null },
      { id: 2, evento: "HUMBE", tipo: "Tour", seccion: "GCE-015", asiento: "F20", fecha: "13 ago, 21:00", precio: 5500.0, imagenUrl: null },
      { id: 3, evento: "HUMBE", tipo: "Tour", seccion: "GCE-015", asiento: "F20", fecha: "13 ago, 21:00", precio: 5500.0, imagenUrl: null },
      { id: 4, evento: "HUMBE", tipo: "Tour", seccion: "GCE-015", asiento: "F20", fecha: "13 ago, 21:00", precio: 5500.0, imagenUrl: null },
    ],
  };
}

// Confirma el pago y regresa el folio de compra
export async function procesarPago(itemsIds) {
  // const response = await api.post("/api/carrito/pagar", { items: itemsIds });
  // return response.data; // { folio, total }

  // --- Placeholder mientras conectamos el backend real ---
  return { folio: "SRTH-456" };
}