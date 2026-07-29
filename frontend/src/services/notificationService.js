import api from "./api";

const RUTAS_POR_CANAL = {
  MAIL: "/api/notificaciones/prueba-correo",
  SMS: "/api/notificaciones/prueba-sms",
  WHATSAPP: "/api/notificaciones/prueba-whatsapp",
};

export async function enviarNotificacionPrueba({ canal, destinatario, mensaje }) {
  const ruta = RUTAS_POR_CANAL[canal];
  if (!ruta) throw new Error(`Canal de notificación no soportado: ${canal}`);

  const response = await api.post(ruta, {
    destino: destinatario,
    mensaje,
  });
  return response.data;
}