import api from "./api";

// --- Helpers de mapeo backend -> forma que usa el frontend ---

function formatearFecha(fecha, hora) {
  if (!fecha) return "Sin fecha";
  try {
    const iso = hora ? `${fecha}T${hora}` : fecha;
    return new Date(iso).toLocaleDateString("es-MX", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    });
  } catch {
    return fecha;
  }
}

// Trae el detalle completo de un evento (fechas + boletos) y lo combina con
// su resumen para poder pintar la fila de la tabla del dashboard.
//
// NOTA: el backend actual (EventoResumenResponse / EventoResponse) NO expone
// cuántos boletos se han VENDIDO ni las ganancias reales de cada evento, porque
// eso requeriría cruzar boleto_evento con las compras (algo que hoy vive en el
// módulo de compras y no está agregado en ningún endpoint). Mientras ese
// endpoint no exista, mostramos en su lugar datos que sí son reales:
// "boletos disponibles" (capacidad restante) e "ingreso potencial"
// (precio x cantidad disponible, sumado por todas las zonas).
async function enriquecerEvento(resumen) {
  try {
    const { data: detalle } = await api.get(`/api/eventos/${resumen.id}`);

    const proximaFecha = [...(detalle.fechas || [])].sort((a, b) =>
      `${a.fecha}${a.hora}`.localeCompare(`${b.fecha}${b.hora}`)
    )[0];

    const boletos = detalle.boletos || [];
    const boletosDisponibles = boletos.reduce((acc, b) => acc + (b.cantidadDisponible || 0), 0);
    const ingresoPotencial = boletos.reduce(
      (acc, b) => acc + Number(b.precio || 0) * (b.cantidadDisponible || 0),
      0
    );

    return {
      id: resumen.id,
      nombre: resumen.nombre,
      estado: resumen.estado,
      fecha: formatearFecha(proximaFecha?.fecha, proximaFecha?.hora),
      // boletosVendidos queda null a propósito: no lo tenemos todavía (ver nota arriba)
      boletosVendidos: null,
      boletosTotales: boletosDisponibles,
      ganancia: ingresoPotencial,
    };
  } catch (error) {
    console.error(`No se pudo cargar el detalle del evento ${resumen.id}:`, error);
    return {
      id: resumen.id,
      nombre: resumen.nombre,
      estado: resumen.estado,
      fecha: "—",
      boletosVendidos: null,
      boletosTotales: null,
      ganancia: null,
    };
  }
}

// pagina es 1-based (así lo usa Dashboard.jsx); el backend usa "page" 0-based
export async function getMisEventos({ pagina, limite = 5 }) {
  const { data } = await api.get("/api/eventos/mios", {
    params: { page: pagina - 1, size: limite },
  });

  const eventos = await Promise.all((data.content || []).map(enriquecerEvento));

  const eventosActivos = eventos.filter((e) => e.estado === "PROGRAMADO").length;
  const gananciasTotales = eventos.reduce((acc, e) => acc + (e.ganancia || 0), 0);

  return {
    // Estas métricas se calculan solo con los eventos de la página actual: el
    // backend no tiene (todavía) un endpoint de métricas agregadas del organizador.
    metricas: {
      gananciasTotales,
      boletosVendidos: null, // no disponible: requiere cruzar con /api/compras
      eventosActivos,
      ocupacion: null, // no disponible por la misma razón
    },
    eventos,
    totalPaginas: data.totalPages ?? 1,
  };
}

// Trae un evento existente para precargar el formulario en modo edición.
// Devuelve también "fechas" y "boletos" tal como los maneja el backend real.
export async function getEventoParaEditar(eventoId) {
  const { data } = await api.get(`/api/eventos/${eventoId}`);
  return data; // { id, nombre, descripcion, estado, flyerPrincipal, fechas, boletos }
}

// Crea un evento nuevo. `datos` debe traer: nombre, descripcion, flyerPrincipal,
// fechas: [{ fecha, hora, ciudad, recintoId }], boletos: [{ zonaId, precio, cantidadDisponible }]
export async function crearEvento(datos) {
  const { data } = await api.post("/api/eventos", datos);
  return data;
}

// El backend real todavía NO tiene un endpoint para editar los datos completos
// de un evento (nombre, fechas, zonas, etc.) — solo existe
// PATCH /api/eventos/{id}/estado para cambiar su estado
// (PROGRAMADO / AGOTADO / CANCELADO / FINALIZADO).
// Por eso, en modo edición, lo único que este servicio puede guardar de verdad
// es el estado del evento.
export async function cambiarEstadoEvento(eventoId, estado) {
  const { data } = await api.patch(`/api/eventos/${eventoId}/estado`, { estado });
  return data;
}

// Mantiene compatibilidad con el nombre que ya usaba CrearEditarEvento.jsx.
// eventoId === null -> crea. eventoId presente -> solo puede cambiar el estado
// (ver nota de cambiarEstadoEvento).
export async function guardarEvento(datos, eventoId = null) {
  if (eventoId) {
    return cambiarEstadoEvento(eventoId, datos.estado);
  }
  return crearEvento(datos);
}
