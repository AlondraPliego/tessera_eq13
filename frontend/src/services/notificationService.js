import api from "./api";

export async function enviarNotificacionPrueba(datos) {
  const response = await api.post("/api/notificaciones/prueba", datos);
  return response.data;
}