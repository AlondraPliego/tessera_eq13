import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import logo from "../assets/img/logo.png";
import recintoImg from "../assets/img/recinto-empresa.jpg";
import { useValidacion } from "../hooks/useValidacion";
import { requerido, esEmail, contrasenaSegura, confirmaCampo } from "../hooks/validadores";
import FormField from "../components/FormField";
import api from "../services/api";
import "./RegistroEmpresa.css";

export default function RegistroEmpresa() {
  const { form, errores, handleChange, validar } = useValidacion(
    {
      nombreEmpresa: "",
      nombreResponsable: "",
      rfc: "",
      correoCorporativo: "",
      telefono: "",
      contrasena: "",
      sitioWeb: "",
      confirmarContrasena: "",
    },
    {
      nombreEmpresa: [requerido],
      nombreResponsable: [requerido],
      rfc: [requerido],
      correoCorporativo: [requerido, esEmail],
      telefono: [requerido],
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
        nombre: form.nombreResponsable,
        nombreEmpresa: form.nombreEmpresa,
        email: form.correoCorporativo,
        password: form.contrasena,
        rolNombre: "EMPRESA",
        rfc: form.rfc,
        telefono: form.telefono,
        sitioWeb: form.sitioWeb,
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
    <div className="registro-empresa-page">
      <div className="registro-empresa-logo">
        <img src={logo} alt="Tessera" className="registro-empresa-logo-img" />
        <span>Tessera</span>
      </div>

      <h1 className="registro-empresa-title">Registro para empresas</h1>

      <div className="registro-empresa-content">
        <div className="registro-empresa-info-side">
          <div className="registro-empresa-welcome-card">
            <p>
              Únete a nuestra red de organizadores. Gestiona tus recintos,
              publica tus eventos y vende boletos de manera segura y
              eficiente.
            </p>
          </div>
          <img
            src={recintoImg}
            alt="Recinto"
            className="registro-empresa-hero-img"
          />
        </div>

        <form onSubmit={handleSubmit} noValidate className="registro-empresa-card">
          <div className="registro-empresa-grid">
            <FormField
              icon="ti-building"
              type="text"
              name="nombreEmpresa"
              placeholder="Nombre de la empresa"
              value={form.nombreEmpresa}
              onChange={handleChange}
              error={errores.nombreEmpresa}
            />
            <FormField
              icon="ti-user"
              type="text"
              name="nombreResponsable"
              placeholder="Nombre del responsable"
              value={form.nombreResponsable}
              onChange={handleChange}
              error={errores.nombreResponsable}
            />
            <FormField
              icon="ti-id"
              type="text"
              name="rfc"
              placeholder="RFC"
              value={form.rfc}
              onChange={handleChange}
              error={errores.rfc}
            />
            <FormField
              icon="ti-at"
              type="email"
              name="correoCorporativo"
              placeholder="Correo corporativo"
              value={form.correoCorporativo}
              onChange={handleChange}
              error={errores.correoCorporativo}
            />
            <FormField
              icon="ti-phone"
              type="tel"
              name="telefono"
              placeholder="Teléfono de la empresa"
              value={form.telefono}
              onChange={handleChange}
              error={errores.telefono}
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
              icon="ti-world"
              type="text"
              name="sitioWeb"
              placeholder="Sitio web"
              value={form.sitioWeb}
              onChange={handleChange}
              error={errores.sitioWeb}
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
          </div>

          {errorApi && <p className="input-error">{errorApi}</p>}

          <button type="submit" className="btn-navy registro-empresa-submit" disabled={enviando}>
            {enviando ? "Registrando..." : "Registrarme"}
          </button>

          <p className="registro-empresa-login-text">
            ¿Ya tienes una cuenta? <Link to="/login">Inicia sesión aquí</Link>
          </p>
        </form>
      </div>
    </div>
  );
}