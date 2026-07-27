import api from "./api";

// Trae los eventos recomendados, con filtro de categoría y paginación
export async function getEventosRecomendados({ categoria, pagina, limite = 6 }) {
  // const response = await api.get("/api/eventos", {
  //   params: { categoria, pagina, limite },
  // });
  // return response.data; // se espera algo como { eventos: [...], totalPaginas: 5 }

  // --- Placeholder mientras conectamos el backend real ---
  const eventosFalsos = Array.from({ length: limite }, (_, i) => ({
    id: `${pagina}-${i}`,
    nombreEvento: `EVENT${(pagina - 1) * limite + i + 1}`,
    nombreRecinto: `EVENT${(pagina - 1) * limite + i + 1}`,
    imagenUrl: null,
  }));

  return { eventos: eventosFalsos, totalPaginas: 5 };
}

// Trae el evento destacado del mes para el banner principal
export async function getEventoDestacado() {
  // const response = await api.get("/api/eventos/destacado");
  // return response.data;

  return null; // por ahora el banner usa la imagen estática que ya tienes
}


// Trae el detalle completo de un evento para el modal
export async function getDetalleEvento(eventoId) {
  // const response = await api.get(`/api/eventos/${eventoId}`);
  // return response.data;
  // Se espera algo como:
  // {
  //   nombreEvento, descripcion, imagenUrl,
  //   fechas: [{ id, dia, mes, diaSemana, hora, recinto, nombreFuncion }]
  // }

  // --- Placeholder mientras conectamos el backend real ---
  return {
    nombreEvento: "Bad Bunny en México",
    descripcion:
      "El Tour de DtMF te transporta directo al corazón de un carnaval puertorriqueño, reviviendo la pura gozadera de DeBÍ TiRAR MáS FOToS.",
    imagenUrl: null, // cuando venga del backend, aquí llega la URL real
    fechas: [
      { id: 1, mes: "Sep", dia: "12", diaSemana: "Sáb", hora: "5:00 pm", recinto: "CMDX", nombreFuncion: "DeBÍ TiRAR MáS FOToS" },
      { id: 2, mes: "Sep", dia: "13", diaSemana: "Dom", hora: "8:00 pm", recinto: "CMDX", nombreFuncion: "DeBÍ TiRAR MáS FOToS" },
      { id: 3, mes: "Sep", dia: "19", diaSemana: "Sáb", hora: "5:00 pm", recinto: "CMDX", nombreFuncion: "DeBÍ TiRAR MáS FOToS" },
    ],
  };
}