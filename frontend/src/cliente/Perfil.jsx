import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import { useValidacion } from "../hooks/useValidacion";
import { requerido, esEmail } from "../hooks/validadores";
import FormField from "../components/FormField";
import api from "../services/api";
import "./Perfil.css";
import { enviarNotificacionPrueba } from '../services/notificationService';

const FORM_VACIO = {
  nombre: "",
  apellidos: "",
  correo: "",
  telefono: "",
  fechaNacimiento: "",
};

export default function Perfil() {
  const navigate = useNavigate();
  const [cargando, setCargando] = useState(true);
  const [errorCarga, setErrorCarga] = useState("");
  const [guardando, setGuardando] = useState(false);

  const { form, errores, handleChange, validar, setForm } = useValidacion(
    FORM_VACIO,
    {
      nombre: [requerido],
      apellidos: [requerido],
      correo: [requerido, esEmail],
      telefono: [requerido],
    }
  );

  useEffect(() => {
    let activo = true;

    const cargarPerfil = async () => {
      try {
        const { data } = await api.get("/api/usuario/perfil");
        if (!activo) return;
        setForm({
          nombre: data.nombre ?? "",
          apellidos: data.apellidos ?? "",
          correo: data.correo ?? "",
          telefono: data.telefono ?? "",
          fechaNacimiento: data.fechaNacimiento ?? "",
        });
      } catch (error) {
        console.error("Error al cargar el perfil:", error);
        if (activo) {
          setErrorCarga("No se pudo cargar tu información. Intenta recargar la página.");
        }
      } finally {
        if (activo) setCargando(false);
      }
    };

    cargarPerfil();
    return () => {
      activo = false;
    };
  }, [setForm]);

  const handleProbarNotificacion = async (canal, destinatario) => {
    try {
      await enviarNotificacionPrueba({
        canal: canal, // 'WHATSAPP', 'SMS' o 'MAIL'
        destinatario: destinatario, // Número de teléfono o correo
        mensaje: "¡Hola! Esta es una prueba de tus boletos desde TESSERA."
      });
      alert(`Notificación enviada exitosamente por ${canal}`);
    } catch (error) {
      console.error("Error al enviar la notificación:", error);
      alert("No se pudo enviar la notificación");
    }
  };

  const handleGuardar = async () => {
    if (!validar()) return;
    setGuardando(true);
    try {
      const { data } = await api.put("/api/usuario/perfil", form);
      setForm({
        nombre: data.nombre ?? "",
        apellidos: data.apellidos ?? "",
        correo: data.correo ?? "",
        telefono: data.telefono ?? "",
        fechaNacimiento: data.fechaNacimiento ?? "",
      });
      alert("Perfil actualizado correctamente");
    } catch (error) {
      console.error("Error al guardar el perfil:", error);
      alert(
        error.response?.data?.message ||
          "No se pudieron guardar los cambios. Intenta de nuevo."
      );
    } finally {
      setGuardando(false);
    }
  };

  const handleCancelar = async () => {
    try {
      const { data } = await api.get("/api/usuario/perfil");
      setForm({
        nombre: data.nombre ?? "",
        apellidos: data.apellidos ?? "",
        correo: data.correo ?? "",
        telefono: data.telefono ?? "",
        fechaNacimiento: data.fechaNacimiento ?? "",
      });
    } catch (error) {
      console.error("Error al recargar el perfil:", error);
    }
  };

  const handleCerrarSesion = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/login");
  };

  return (
    <div className="perfil-page">
      <div className="perfil-topbar">
        <Link to="/" className="perfil-logo">
          <img src={logo} alt="Tessera" className="perfil-logo-img" />
          Tessera
        </Link>

        <div className="perfil-search">
          <input type="text" placeholder="Busca artista, evento o recinto" />
          <i className="ti ti-search"></i>
        </div>

        <div className="perfil-nav-links">
          <Link to="/mis-boletos">Mis boletos</Link>
          <Link to="/perfil" className="perfil-usuario-btn active">
            <i className="ti ti-user"></i>
            Usuario
          </Link>
        </div>
      </div>

      <div className="perfil-header">
        <h1 className="perfil-title">Mi cuenta</h1>
        <p className="perfil-subtitle">Administra tus datos personales y preferencias.</p>
      </div>

      <div className="perfil-content">
        <div className="perfil-sidebar">
          <div className="perfil-avatar-block">
            <div className="perfil-avatar">
              <i className="ti ti-user"></i>
            </div>
            <p className="perfil-avatar-nombre">
              {form.nombre} {form.apellidos.split(" ")[0]?.[0]}.
            </p>
            <p className="perfil-avatar-rol">Cliente</p>
          </div>

          <nav className="perfil-menu">
            <span className="perfil-menu-item active">
              <i className="ti ti-user"></i> Datos personales
            </span>
            <span className="perfil-menu-item">
              <i className="ti ti-lock"></i> Seguridad
            </span>
            <span className="perfil-menu-item">
              <i className="ti ti-wallet"></i> Métodos de pago
            </span>
            <Link to="/mis-boletos" className="perfil-menu-item">
              <i className="ti ti-ticket"></i> Mis boletos
            </Link>
            <button className="perfil-menu-item logout" onClick={handleCerrarSesion}>
              <i className="ti ti-logout"></i> Cerrar sesión
            </button>
          </nav>
        </div>

        <div className="perfil-form-card">
          <h2 className="perfil-form-title">Datos personales</h2>

          {cargando && <p className="perfil-subtitle">Cargando tu información…</p>}
          {errorCarga && <p className="input-error">{errorCarga}</p>}

          <div className="perfil-form-grid">
            <FormField
              icon="ti-user"
              type="text"
              name="nombre"
              placeholder="Nombre"
              value={form.nombre}
              onChange={handleChange}
              error={errores.nombre}
              label="Nombre"
            />
            
          </div>

          <FormField
            icon="ti-at"
            type="email"
            name="correo"
            placeholder="Correo electrónico"
            value={form.correo}
            onChange={handleChange}
            error={errores.correo}
            label="Correo electrónico"
          />

          <div className="perfil-form-grid">
            <FormField
              icon="ti-phone"
              type="tel"
              name="telefono"
              placeholder="Teléfono"
              value={form.telefono}
              onChange={handleChange}
              error={errores.telefono}
              label="Teléfono"
            />

          </div>

          <div className="perfil-form-buttons">
            <button
              className="perfil-btn-cancelar"
              onClick={handleCancelar}
              disabled={cargando || guardando}
            >
              Cancelar
            </button>
            <button
              className="perfil-btn-guardar"
              onClick={handleGuardar}
              disabled={cargando || guardando}
            >
              {guardando ? "Guardando..." : "Guardar cambios"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}