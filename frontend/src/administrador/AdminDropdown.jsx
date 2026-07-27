import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getAdminActual } from "../services/adminService";
import "./AdminDropdown.css";

export default function AdminDropdown({ onClose }) {
  const navigate = useNavigate();
  const [admin, setAdmin] = useState(null);

  useEffect(() => {
    async function cargar() {
      try {
        const data = await getAdminActual();
        setAdmin(data);
      } catch (error) {
        console.error("Error al cargar el admin:", error);
      }
    }
    cargar();
  }, []);

  const handleCerrarSesion = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    onClose();
    navigate("/");
  };

  return (
    <div className="admin-dropdown-overlay" onClick={onClose}>
      <div className="admin-dropdown-card" onClick={(e) => e.stopPropagation()}>
        <div className="admin-dropdown-header">
          <div className="admin-dropdown-avatar">
            <i className="ti ti-user"></i>
          </div>
          <div>
            <p className="admin-dropdown-nombre">{admin?.nombre || "Cargando..."}</p>
            <p className="admin-dropdown-rol">{admin?.rol}</p>
          </div>
        </div>

        <button className="admin-dropdown-logout" onClick={handleCerrarSesion}>
          Cerrar sesión
        </button>
      </div>
    </div>
  );
}