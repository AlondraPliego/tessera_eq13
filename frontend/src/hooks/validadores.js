export const requerido = (valor) => {
  if (!valor || !valor.trim()) return "Completa este campo.";
  return null;
};

export const esEmail = (valor) => {
  if (!/\S+@\S+\.\S+/.test(valor)) return "Ingresa un correo válido.";
  return null;
};

export const minLength = (min) => (valor) => {
  if (valor && valor.length < min) return `Debe tener al menos ${min} caracteres.`;
  return null;
};

export const confirmaCampo = (nombreCampo, mensaje = "No coinciden.") => (valor, formCompleto) => {
  if (valor && valor !== formCompleto[nombreCampo]) return mensaje;
  return null;
};

export const contrasenaSegura = (valor) => {
  const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
  if (valor && !regex.test(valor)) {
    return "Mínimo 8 caracteres, una mayúscula, un número y un carácter especial.";
  }
  return null;
};