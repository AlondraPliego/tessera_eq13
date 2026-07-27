import api from "./api";

// Boletos activos del usuario (eventos que aún no han pasado)
export async function getBoletosActivos() {
  // const response = await api.get("/api/boletos/activos");
  // return response.data;

  // --- Placeholder mientras conectamos el backend real ---
  return [
    {
      id: 1,
      evento: "HUMBE",
      tour: "Dueño del cielo Tour",
      tipo: "Tour",
      seccion: "GCE-015",
      asiento: "F20",
      fecha: "13 ago, 21:00",
      posterUrl: null,
    },
  ];
}

// Historial de eventos ya finalizados (se determina comparando la fecha del evento contra hoy)
export async function getHistorialEventos() {
  // const response = await api.get("/api/boletos/historial");
  // return response.data;

  // --- Placeholder mientras conectamos el backend real ---
  return [
    { id: 1, evento: "BTS TOUR -- ARIRANG", cantidadBoletos: 3 },
    { id: 2, evento: "BTS TOUR -- ARIRANG", cantidadBoletos: 3 },
    { id: 3, evento: "BTS TOUR -- ARIRANG", cantidadBoletos: 3 },
  ];
}