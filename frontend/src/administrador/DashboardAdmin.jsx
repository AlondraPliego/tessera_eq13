import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getResumenAdmin, actualizarEstadoSolicitud } from "../services/adminService";
import AdminDropdown from "./AdminDropdown";
import "./DashboardAdmin.css";

export default function DashboardAdmin() {
  const navigate = useNavigate();
  const [dropdownAbierto, setDropdownAbierto] = useState(false);
  const [datos, setDatos] = useState(null);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    async function cargar() {
      setCargando(true);
      try {
        const data = await getResumenAdmin();
        setDatos(data);
      } catch (error) {
        console.error("Error al cargar el dashboard de admin:", error);
      } finally {
        setCargando(false);
      }
    }
    cargar();
  }, []);

  const handleAceptarSolicitud = async (id) => {
    try {
      await actualizarEstadoSolicitud(id, "Aceptado");
      setDatos((prev) => ({
        ...prev,
        solicitudes: prev.solicitudes.map((s) =>
          s.id === id ? { ...s, estado: "Aceptado" } : s
        ),
      }));
    } catch (error) {
      console.error("Error al aceptar la solicitud:", error);
    }
  };

  if (cargando || !datos) {
    return <p className="admin-loading">Cargando...</p>;
  }

  return (
    <div className="admin-page">
      <div className="admin-topbar">
        <span className="admin-logo" onClick={() => navigate("/")}>
          Tessera
        </span>

        <div className="admin-nav-right">
          <div className="admin-dropdown-wrapper">
            <button
              className="admin-usuario-btn"
              onClick={() => setDropdownAbierto((prev) => !prev)}
            >
              <i className="ti ti-user"></i>
              Usuario
              <i className="ti ti-chevron-down"></i>
            </button>

            {dropdownAbierto && (
              <AdminDropdown onClose={() => setDropdownAbierto(false)} />
            )}
          </div>
        </div>
      </div>

      <div className="admin-header">
        <h1 className="admin-title">Administración general</h1>
        <p className="admin-subtitle">Validación de organizadores</p>
      </div>

      <div className="admin-metrics">
        <div className="admin-metric-card">
          <span className="admin-metric-label">Usuarios totales</span>
          <span className="admin-metric-value">{datos.usuariosTotales}</span>
        </div>
        <div className="admin-metric-card">
          <span className="admin-metric-label">Registros pendientes</span>
          <span className="admin-metric-value">{datos.registrosPendientes}</span>
        </div>
        <div className="admin-metric-card">
          <span className="admin-metric-label">Promotores activos</span>
          <span className="admin-metric-value">{datos.promotoresActivos}</span>
        </div>
      </div>

      <div className="admin-panels">
        <div className="admin-panel">
          <h2 className="admin-panel-title">Solicitudes de registro</h2>
          <div className="admin-panel-list">
            {datos.solicitudes.map((s) => (
              <div key={s.id} className="admin-list-row">
                <span className="admin-list-badge">{s.id}</span>
                <div className="admin-list-info">
                  <p>{s.empresa}</p>
                  <p className="admin-list-fecha">{s.fecha}</p>
                </div>
                <button
                  className={`admin-estado-pill ${s.estado === "Aceptado" ? "aceptado" : ""}`}
                  onClick={() =>
                    s.estado === "Pendiente" ? handleAceptarSolicitud(s.id) : undefined
                  }
                  disabled={s.estado === "Aceptado"}
                >
                  {s.estado}
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="admin-panel">
          <h2 className="admin-panel-title">Usuarios promotores</h2>
          <div className="admin-panel-list">
            {datos.promotores.map((p) => (
              <div key={p.id} className="admin-list-row">
                <span className="admin-list-badge">{p.id}</span>
                <div className="admin-list-info">
                  <p>{p.empresa}</p>
                  <p className="admin-list-fecha">{p.rol}</p>
                </div>
                <span
                  className={`admin-estado-pill estado-info ${
                    p.estado === "Activo" ? "activo" : "desconectado"
                  }`}
                >
                  {p.estado}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}