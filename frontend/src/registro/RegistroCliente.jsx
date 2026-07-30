import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import logo from "../assets/img/logo.png";
import estadioImg from "../assets/img/estadio.png";
import { useValidacion } from "../hooks/useValidacion";
import { requerido, esEmail, contrasenaSegura, confirmaCampo } from "../hooks/validadores";
import FormField from "../components/FormField";
import api from "../services/api";
import "./RegistroCliente.css";

export default function RegistroCliente() {
  const { form, errores, handleChange, validar } = useValidacion(
    {
      nombreCompleto: "",
      nombreUsuario: "",
      telefonoUsuario: "",
      correo: "",
      contrasena: "",
      confirmarContrasena: "",
    },
    {
      nombreCompleto: [requerido],
      nombreUsuario: [requerido],
      correo: [requerido, esEmail],
      contrasena: [requerido, contrasenaSegura],
      confirmarContrasena: [requerido, confirmaCampo("contrasena", "Las contraseñas no coinciden.")],
    }
  );

  const [errorApi, setErrorApi] = useState("");
  const [enviando, setEnviando] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validar()) return;

    setErrorApi("");
    setEnviando(true);
    try {
      await api.post("/api/auth/register", {
        nombre: form.nombreCompleto,
        nombreUsuario: form.nombreUsuario,
        telefono: form.telefonoUsuario,
        email: form.correo,
        password: form.contrasena,
        rolNombre: "CLIENTE",
      });
      navigate("/login");
    } catch (err) {
      setErrorApi(
        err.response?.data?.message ||
          err.response?.data ||
          "No se pudo completar el registro."
      );
    } finally {
      setEnviando(false);
    }
  };

  return (
    <div className="registro-page">
      <div className="registro-logo">
        <img src={logo} alt="Tessera" className="registro-logo-img" />
        <span>Tessera</span>
      </div>

      <div className="registro-content">
        <div className="registro-welcome-side">
          <h1 className="registro-title">Bienvenido</h1>
          <div className="registro-welcome-card">
            <p>
              Compra boletos para miles de eventos en vivo, descubre
              conciertos inolvidables, juegos, teatro y más. Todo ello con un
              sistema de entradas seguro y sencillo.
            </p>
            <Link to="/registro/empresa" className="registro-empresa-link">
              ¿Eres una empresa? Regístrate aquí
            </Link>
          </div>
          <img src={estadioImg} alt="Recinto" className="registro-hero-img" />
        </div>

        <div className="registro-form-side">
          <h1 className="registro-title">Registro</h1>

          <form onSubmit={handleSubmit} noValidate className="registro-card">
            <FormField
              icon="ti-user"
              type="text"
              name="nombreCompleto"
              placeholder="Nombre completo"
              value={form.nombreCompleto}
              onChange={handleChange}
              error={errores.nombreCompleto}
            />
            <FormField
              icon="ti-user"
              type="text"
              name="nombreUsuario"
              placeholder="Nombre de usuario"
              value={form.nombreUsuario}
              onChange={handleChange}
              error={errores.nombreUsuario}
            />
            <FormField
              icon="ti-user"
              type="text"
              name="telefonoUsuario"
              placeholder="Teléfono"
              value={form.telefonoUsuario}
              onChange={handleChange}
              error={errores.telefonoUsuario}
            />
            <FormField
              icon="ti-at"
              type="email"
              name="correo"
              placeholder="Correo"
              value={form.correo}
              onChange={handleChange}
              error={errores.correo}
            />
            <FormField
              icon="ti-lock"
              type="password"
              name="contrasena"
              placeholder="Contraseña"
              value={form.contrasena}
              onChange={handleChange}
              error={errores.contrasena}
            />
            <FormField
              icon="ti-lock"
              type="password"
              name="confirmarContrasena"
              placeholder="Confirmar contraseña"
              value={form.confirmarContrasena}
              onChange={handleChange}
              error={errores.confirmarContrasena}
            />

            {errorApi && <p className="input-error">{errorApi}</p>}

            <button type="submit" className="btn-navy" disabled={enviando}>
              {enviando ? "Registrando..." : "Registrarme"}
            </button>

            <p className="registro-login-text">
              ¿Ya tienes una cuenta? <Link to="/login">Inicia sesión aquí</Link>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
}