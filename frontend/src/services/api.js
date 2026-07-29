import axios from "axios";

// Cambia esto por la URL de tu backend Spring Boot (local o del VPS)
const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";
export { BASE_URL };

export function resolverUrlImagen(ruta) {
  if (!ruta) return ruta;
  if (ruta.startsWith("http://") || ruta.startsWith("https://")) return ruta;
  return `${BASE_URL}${ruta}`;
}
const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// --- Interceptor de peticiones: agrega el token JWT en cada request ---
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// --- Interceptor de respuestas: si el token expiró o es inválido, cierra sesión ---
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export default api;