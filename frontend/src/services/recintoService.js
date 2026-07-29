import api from "./api";

// Recintos de la empresa logueada (para elegir dónde será la función del evento)
export async function getMisRecintos() {
  const { data } = await api.get("/api/recintos/mios", { params: { size: 100 } });
  return data.content || [];
}

// Zonas ya definidas para un recinto (para elegir sección + precio del boleto)
export async function getZonasDeRecinto(recintoId) {
  if (!recintoId) return [];
  const { data } = await api.get(`/api/recintos/${recintoId}/zonas`);
  return data || [];
}
