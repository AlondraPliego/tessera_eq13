import api from "./api";

export async function getResumenAdmin() {
  // const response = await api.get("/api/admin/resumen");
  // return response.data;

  // --- Placeholder mientras conectamos el backend real ---
  return {
    usuariosTotales: 1200,
    registrosPendientes: 3,
    promotoresActivos: 14,
    solicitudes: [
      { id: 1, empresa: "Empresa", fecha: "26/07/2026", estado: "Pendiente" },
      { id: 2, empresa: "Empresa", fecha: "25/07/2026", estado: "Pendiente" },
      { id: 3, empresa: "Empresa", fecha: "24/07/2026", estado: "Pendiente" },
      { id: 4, empresa: "Empresa", fecha: "23/07/2026", estado: "Pendiente" },
      { id: 5, empresa: "Empresa", fecha: "22/07/2026", estado: "Aceptado" },
    ],
    promotores: [
      { id: 1, empresa: "Empresa", rol: "Representante", estado: "Desconectado" },
      { id: 2, empresa: "Empresa", rol: "Representante", estado: "Activo" },
      { id: 3, empresa: "Empresa", rol: "Representante", estado: "Activo" },
      { id: 4, empresa: "Empresa", rol: "Representante", estado: "Activo" },
      { id: 5, empresa: "Empresa", rol: "Representante", estado: "Activo" },
    ],
  };
}

export async function actualizarEstadoSolicitud(solicitudId, nuevoEstado) {
  // const response = await api.patch(`/api/admin/solicitudes/${solicitudId}`, { estado: nuevoEstado });
  // return response.data;

  console.log("Actualizar solicitud", solicitudId, "a", nuevoEstado);
  return { id: solicitudId, estado: nuevoEstado };
}

export async function getAdminActual() {
  // const response = await api.get("/api/admin/perfil");
  // return response.data;

  return { nombre: "Leonel Isaac", rol: "Administrador General" };
}