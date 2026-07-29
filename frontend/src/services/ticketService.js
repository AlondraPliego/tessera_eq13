import api from "./api";

// El backend no guarda un "asiento" individual (esa asignación puntual vive en
// seatmap.pro y se resuelve solo durante la reserva, que se borra al confirmar
// la compra). Lo más específico que tenemos es la sección/zona comprada y la
// cantidad de boletos de esa compra, así que usamos eso en vez de inventar un
// número de asiento.
async function obtenerMisBoletos() {
  const { data } = await api.get("/api/compras/mias/boletos");
  return data || [];
}

function formatearFecha(fecha, hora) {
  if (!fecha) return "Fecha por confirmar";
  try {
    const iso = hora ? `${fecha}T${hora}` : fecha;
    return new Date(iso).toLocaleDateString("es-MX", {
      day: "2-digit",
      month: "short",
      hour: hora ? "2-digit" : undefined,
      minute: hora ? "2-digit" : undefined,
    });
  } catch {
    return fecha;
  }
}

function esFuturo(fecha) {
  if (!fecha) return true; // si no tiene fecha aún, lo tratamos como activo
  return new Date(fecha) >= new Date(new Date().toDateString());
}

// Boletos activos del usuario (eventos que aún no han pasado)
export async function getBoletosActivos() {
  const boletos = await obtenerMisBoletos();

  return boletos
    .filter((b) => b.estadoCompra !== "CANCELADA" && esFuturo(b.fecha))
    .map((b) => ({
      id: b.detalleCompraId,
      evento: b.evento,
      tour: b.descripcionEvento || "",
      tipo: `x${b.cantidad}`,
      seccion: b.seccion || "—",
      asiento: `${b.cantidad} boleto${b.cantidad === 1 ? "" : "s"}`,
      fecha: formatearFecha(b.fecha, b.hora),
      posterUrl: b.flyerPrincipal || null,
    }));
}

// Historial de eventos ya finalizados (fecha del evento anterior a hoy)
export async function getHistorialEventos() {
  const boletos = await obtenerMisBoletos();

  const pasados = boletos.filter((b) => b.estadoCompra !== "CANCELADA" && !esFuturo(b.fecha));

  // Agrupamos por evento, ya que un mismo evento puede tener varias compras/zonas
  const porEvento = new Map();
  for (const b of pasados) {
    const actual = porEvento.get(b.eventoId) || { id: b.eventoId, evento: b.evento, cantidadBoletos: 0 };
    actual.cantidadBoletos += b.cantidad;
    porEvento.set(b.eventoId, actual);
  }

  return Array.from(porEvento.values());
}
