import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";
import "./PerfilComercialDropdown.css";

export default function PerfilComercialDropdown({ onClose }) {
  const navigate = useNavigate();
  const [empresa, setEmpresa] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let activo = true;

    api
      .get("/api/usuario/perfil-empresa")
      .then(({ data }) => {
        if (activo) setEmpresa(data);
      })
      .catch((err) => {
        console.error("Error al cargar el perfil de la empresa:", err);
        if (activo) setError("No se pudo cargar tu información.");
      })
      .finally(() => {
        if (activo) setCargando(false);
      });

    return () => {
      activo = false;
    };
  }, []);

  const handleCerrarSesion = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    onClose();
    navigate("/");
  };

  return (
    <div className="dropdown-overlay" onClick={onClose}>
      <div className="dropdown-card" onClick={(e) => e.stopPropagation()}>
        <div className="dropdown-header">
          <div className="dropdown-avatar">
            <i className="ti ti-building"></i>
          </div>
          <div>
            <p className="dropdown-empresa">
              {cargando ? "Cargando..." : empresa?.nombreEmpresa || "—"}
            </p>
            <p className="dropdown-verificado">Organizador verificado</p>
          </div>
        </div>

        <div className="dropdown-fiscal">
          <p className="dropdown-fiscal-label">Perfil comercial</p>
          {error && <p className="dropdown-fiscal-row">{error}</p>}
          <div className="dropdown-fiscal-row">
            <span>Razón social</span>
            <span>{cargando ? "..." : empresa?.nombreEmpresa || "—"}</span>
          </div>
          <div className="dropdown-fiscal-row">
            <span>RFC</span>
            <span>{cargando ? "..." : empresa?.rfc || "—"}</span>
          </div>
        </div>

        <button className="dropdown-logout" onClick={handleCerrarSesion}>
          Cerrar sesión
        </button>
      </div>
    </div>
  );
}