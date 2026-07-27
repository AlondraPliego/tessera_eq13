import { Link, useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import { useValidacion } from "../hooks/useValidacion";
import { requerido, esEmail } from "../hooks/validadores";
import FormField from "../components/FormField";
import "./Perfil.css";

export default function Perfil() {
  const navigate = useNavigate();

  const { form, errores, handleChange, validar, setForm } = useValidacion(
    {
      nombre: "Alondra",
      apellidos: "Pliego Mendez",
      correo: "alondrapliego131104@gmail.com",
      telefono: "9518737324",
      fechaNacimiento: "2004-11-13",
    },
    {
      nombre: [requerido],
      apellidos: [requerido],
      correo: [requerido, esEmail],
      telefono: [requerido],
    }
  );

  const handleGuardar = () => {
    if (!validar()) return;
    // aquí luego conectamos con api.js: api.put("/api/usuario/perfil", form)
    console.log("Guardar perfil:", form);
  };

  const handleCancelar = () => {
    setForm({
      nombre: "Alondra",
      apellidos: "Pliego Mendez",
      correo: "alondrapliego131104@gmail.com",
      telefono: "9518737324",
      fechaNacimiento: "2004-11-13",
    });
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
            <FormField
              icon="ti-user"
              type="text"
              name="apellidos"
              placeholder="Apellidos"
              value={form.apellidos}
              onChange={handleChange}
              error={errores.apellidos}
              label="Apellidos"
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
            <FormField
              icon="ti-calendar"
              type="date"
              name="fechaNacimiento"
              value={form.fechaNacimiento}
              onChange={handleChange}
              error={errores.fechaNacimiento}
              label="Fecha de nacimiento"
            />
          </div>

          <div className="perfil-form-buttons">
            <button className="perfil-btn-cancelar" onClick={handleCancelar}>
              Cancelar
            </button>
            <button className="perfil-btn-guardar" onClick={handleGuardar}>
              Guardar cambios
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}