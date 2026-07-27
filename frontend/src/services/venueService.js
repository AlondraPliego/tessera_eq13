import api from "./api";

// Trae la info del evento + el recinto + las secciones disponibles para la Ventana 4
export async function getEventoParaAsientos(eventoId, fechaId) {
  // const response = await api.get(`/api/eventos/${eventoId}/asientos`, {
  //   params: { fecha: fechaId },
  // });
  // return response.data;
  // Se espera algo como:
  // {
  //   nombreEvento, imagenUrl, fecha, hora, recinto,
  //   mapaUrl, // imagen o data del mapa que arma la Ventana 11 (recinto)
  //   secciones: [{ id, nombre, tipoBoleto, precio }]
  // }

  // --- Placeholder mientras conectamos el backend real ---
  return {
    nombreEvento: "HUMBE",
    imagenUrl: null,
    fecha: "13 de agosto",
    hora: "21:00",
    recinto: "Estadio Guelaguetza, Oaxaca de Juárez",
    mapaUrl: null, // vendrá de la entidad Recinto (Ventana 11)
    secciones: [
      { id: 1, nombre: "Sección GENERAL C", tipo: "Boleto normal", precio: 1463.25 },
      { id: 2, nombre: "Sección GENERAL C", tipo: "Boleto normal", precio: 1463.25 },
      { id: 3, nombre: "Sección GENERAL C", tipo: "Boleto normal", precio: 1463.25 },
      { id: 4, nombre: "Sección GENERAL C", tipo: "Boleto normal", precio: 1463.25 },
    ],
  };
}

// CRUD de recintos en TU backend (que a su vez guarda el schemaId de Seatmap Pro)
export async function getRecintos() {
  // const response = await api.get("/api/admin/recintos");
  // return response.data;

  return [
    { id: 1, nombre: "Estadio Guelaguetza", direccion: "Oaxaca de Juárez, Oax.", schemaId: 3275, publicKey: "pk_demo_123", estado: "Publicado" },
    { id: 2, nombre: "Auditorio Guelaguetza", direccion: "Oaxaca de Juárez, Oax.", schemaId: null, publicKey: null, estado: "Sin mapa" },
  ];
}

export async function crearRecinto(datos) {
  // Tu backend guarda nombre/dirección, y por separado vincula el schemaId
  // que el organizador copia desde el Editor de Seatmap Pro.
  // const response = await api.post("/api/admin/recintos", datos);
  // return response.data;

  console.log("Crear recinto:", datos);
  return { id: Date.now(), ...datos, estado: "Sin mapa" };
}

export async function vincularSchemaSeatmap(recintoId, schemaId, publicKey) {
  // Este endpoint en TU backend Spring Boot guarda la relación recinto <-> schema.
  // El token de organización de Seatmap Pro se usa solo en el backend, nunca aquí.
  // const response = await api.patch(`/api/admin/recintos/${recintoId}/schema`, { schemaId, publicKey });
  // return response.data;

  console.log("Vincular schema de Seatmap Pro:", recintoId, schemaId, publicKey);
  return { recintoId, schemaId, publicKey, estado: "Publicado" };
}