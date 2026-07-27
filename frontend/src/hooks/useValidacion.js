import { useState } from "react";

/**
 *
 * @param {Object} valoresIniciales - ej. { correo: "", contrasena: "" }
 * @param {Object} reglas - ej. { correo: [reglaRequerido, reglaEmail] }
 */
export function useValidacion(valoresIniciales, reglas) {
  const [form, setForm] = useState(valoresIniciales);
  const [errores, setErrores] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));

    // si el campo tenía error y el usuario ya está corrigiendo, lo limpiamos
    if (errores[name]) {
      setErrores((prev) => ({ ...prev, [name]: null }));
    }
  };

  const validar = () => {
    const nuevosErrores = {};

    Object.keys(reglas).forEach((campo) => {
      const validadoresDelCampo = reglas[campo];

      for (const validador of validadoresDelCampo) {
        const mensaje = validador(form[campo], form);
        if (mensaje) {
          nuevosErrores[campo] = mensaje;
          break; // se detiene en el primer error de ese campo
        }
      }
    });

    setErrores(nuevosErrores);
    return Object.keys(nuevosErrores).length === 0;
  };

  const resetForm = () => {
    setForm(valoresIniciales);
    setErrores({});
  };

  return { form, errores, handleChange, validar, resetForm, setForm };
}