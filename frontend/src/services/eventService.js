import api from "./api";

const MESES = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
const DIAS_SEMANA = ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"];

function formatearFecha(fechaISO, horaISO) {
  const fecha = new Date(`${fechaISO}T${horaISO || "00:00:00"}`);
  const [horas, minutos] = (horaISO || "00:00").split(":");
  const horaNum = Number(horas);
  const ampm = horaNum >= 12 ? "pm" : "am";
  const hora12 = ((horaNum + 11) % 12) + 1;

  return {
    dia: String(fecha.getDate()),
    mes: MESES[fecha.getMonth()],
    diaSemana: DIAS_SEMANA[fecha.getDay()],
    hora: `${hora12}:${minutos} ${ampm}`,
  };
}

export async function getEventosRecomendados({ categoria, pagina, limite = 6 }) {
  const response = await api.get("/api/eventos", {
    params: {
      estado: "PUBLICADO", 
      page: pagina - 1,   
      size: limite,
    },
  });

  const pageData = response.data;

  return {
    eventos: pageData.content.map((e) => ({
      id: e.id,
      nombreEvento: e.nombre,
      nombreRecinto: "", 
      imagenUrl: e.flyerPrincipal,
    })),
    totalPaginas: pageData.totalPages,
  };
}

export async function getEventoDestacado() {
  // No existe endpoint /api/eventos/destacado en el backend todavía.
  // Cuando exista, descomenta esto:
  // const response = await api.get("/api/eventos/destacado");
  // return response.data;

  return null
}

// Trae el detalle completo de un evento para el modal
export async function getDetalleEvento(eventoId) {
  const response = await api.get(`/api/eventos/${eventoId}`);
  const data = response.data; // EventoResponse

  return {
    nombreEvento: data.nombre,
    descripcion: data.descripcion,
    imagenUrl: data.flyerPrincipal,
    fechas: (data.fechas || []).map((f) => ({
      id: f.id,
      ...formatearFecha(f.fecha, f.hora),
      recinto: f.ciudad, // solo la ciudad
      nombreFuncion: data.nombre,
    })),
  };
}