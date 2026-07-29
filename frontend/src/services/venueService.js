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
    schemaId: r.seatmapSchemaId,
    estado: r.seatmapSchemaId ? "Publicado" : "Sin mapa",
  }));
}

export async function crearRecinto(datos) {
  const { data } = await api.post("/api/recintos", datos);
  return data;
}

export async function vincularSchemaSeatmap(recintoId, schemaId) {
  const { data } = await api.put(`/api/recintos/${recintoId}`, {
    seatmapSchemaId: schemaId,
  });
  return data;
}
