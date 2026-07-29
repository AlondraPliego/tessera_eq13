import api from "./api";

// El backend real expone POST /api/uploads/flyers (solo EMPRESA), no /api/uploads.
export async function subirImagen(archivo) {
  const formData = new FormData();
  formData.append("file", archivo);

  const response = await api.post("/api/uploads/flyers", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return response.data; // { url }
}
