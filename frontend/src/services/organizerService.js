import api from "./api";

export async function getMisEventos({ pagina, limite = 5 }) {
  // const response = await api.get("/api/organizador/eventos", { params: { pagina, limite } });
  // return response.data; // { metricas, eventos, totalPaginas }

  // --- Placeholder mientras conectamos el backend real ---
  return {
    metricas: {
      gananciasTotales: 184320,
      boletosVendidos: 3413,
      eventosActivos: 6,
      ocupacion: 78,
    },
    eventos: Array.from({ length: limite }, (_, i) => ({
      id: `${pagina}-${i}`,
      nombre: "Beyoncé -- Renaissance Tour",
      fecha: "14 ago 2026",
      boletosVendidos: 2140,
      boletosTotales: 2500,
      ganancia: 96300,
      estado: "En venta",
    })),
    totalPaginas: 5,
  };
}

// Trae un evento existente para precargar el formulario en modo edición
export async function getEventoParaEditar(eventoId) {
  // const response = await api.get(`/api/organizador/eventos/${eventoId}`);
  // return response.data;

  return {
    nombreEvento: "Beyoncé -- Renaissance Tour",
    descripcion: "El Renaissance World Tour llega a México...",
    categoria: "Conciertos",
    fecha: "2026-08-14",
    hora: "20:00",
    recintoId: "1",
    imagenUrl: null,
  };
}

export async function guardarEvento(datos, eventoId = null) {
  // if (eventoId) {
  //   const response = await api.put(`/api/organizador/eventos/${eventoId}`, datos);
  //   return response.data;
  // }
  // const response = await api.post("/api/organizador/eventos", datos);
  // return response.data;

  console.log(eventoId ? "Editando evento" : "Creando evento", datos);
  return { id: eventoId || Date.now() };
}