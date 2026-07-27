import { useNavigate } from "react-router-dom";
import "./PerfilComercialDropdown.css";

export default function PerfilComercialDropdown({ onClose }) {
  const navigate = useNavigate();

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
            <p className="dropdown-empresa">Recintos Culturales S.A de CV</p>
            <p className="dropdown-verificado">Organizador verificado</p>
          </div>
        </div>

        <div className="dropdown-fiscal">
          <p className="dropdown-fiscal-label">Perfil comercial</p>
          <div className="dropdown-fiscal-row">
            <span>Razón social</span>
            <span>Recintos Culturales S.A de C.V</span>
          </div>
          <div className="dropdown-fiscal-row">
            <span>RFC</span>
            <span>RCU850312AB1</span>
          </div>
          <div className="dropdown-fiscal-row">
            <span>Régimen fiscal</span>
            <span>Persona moral</span>
          </div>
        </div>

        <button className="dropdown-logout" onClick={handleCerrarSesion}>
          Cerrar sesión
        </button>
      </div>
    </div>
  );
}