import api from "./api";

export async function subirImagen(archivo) {
  const formData = new FormData();
  formData.append("file", archivo); 

  const response = await api.post("/api/uploads", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
  
  return response.data; 
}