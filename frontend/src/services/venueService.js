import api from "./api";

// esto es momentaneo hasta implementar seatmap
export async function getEventoParaAsientos(eventoId, fechaId) {
  const { data: evento } = await api.get(`/api/eventos/${eventoId}`);

  const fecha =
    evento.fechas.find((f) => String(f.id) === String(fechaId)) || evento.fechas[0];

  const { data: zonas } = await api.get(`/api/recintos/${fecha.recintoId}/zonas`);
  const zonaPorId = Object.fromEntries(zonas.map((z) => [z.id, z]));

  return {
    nombreEvento: evento.nombre,
    imagenUrl: evento.flyerPrincipal,
    fecha: fecha.fecha,
    hora: fecha.hora,
    recinto: fecha.ciudad,
    mapaUrl: null,
    secciones: evento.boletos.map((b) => ({
      id: b.id,
      nombre: zonaPorId[b.zonaId]?.nombre || `Zona ${b.zonaId}`,
      tipo: "Boleto general",
      precio: Number(b.precio),
      disponibles: b.cantidadDisponible,
    })),
  };
}

export async function getRecintos({ pagina = 1, limite = 10 } = {}) {
  const { data } = await api.get("/api/recintos/mios", {
    params: { page: pagina - 1, size: limite },
  });
  return data.content.map((r) => ({
    id: r.id,
    nombre: r.nombre,
    direccion: r.direccion,
    mapaSvg: r.mapaSvg,
    schemaId: r.seatmapSchemaId,
    estado: r.seatmapSchemaId ? "Publicado" : "Sin mapa",
  }));
}

export async function crearRecinto(datos) {
  const { data } = await api.post("/api/recintos", datos);
  return data;
}

// El PUT /api/recintos/{id} usa RecintoRequest completo (nombre y dirección
// son obligatorios en el backend), no acepta un patch parcial con solo el
// seatmapSchemaId. Por eso se manda el recinto completo con el nuevo schemaId.
export async function vincularSchemaSeatmap(recinto, schemaId) {
  const { data } = await api.put(`/api/recintos/${recinto.id}`, {
    nombre: recinto.nombre,
    direccion: recinto.direccion,
    mapaSvg: recinto.mapaSvg || null,
    seatmapSchemaId: Number(schemaId),
  });
  return data;
}
