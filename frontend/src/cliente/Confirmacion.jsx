import { useParams, useLocation, useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import "./Confirmacion.css";

export default function Confirmacion() {
  const { folio } = useParams();
  const { state } = useLocation();
  const navigate = useNavigate();

  // Si el usuario llega directo por URL sin pasar por el carrito, usamos valores por defecto
  const evento = state?.evento || "HUMBE Tour";
  const boletos = state?.boletos ?? 4;
  const total = state?.total ?? 7100.0;

  const handleDescargarComprobante = () => {
    // aquí luego se conecta con la generación real de PDF desde el backend
    console.log("Descargar comprobante del folio", folio);
  };

  return (
    <div className="confirmacion-page">
      <div className="confirmacion-topbar">
        <span className="confirmacion-logo">
          <img src={logo} alt="Tessera" className="confirmacion-logo-img" />
          Tessera
        </span>
        <button className="confirmacion-usuario-btn">
          <i className="ti ti-user"></i>
          Usuario
        </button>
      </div>

      <div className="confirmacion-content">
        <div className="confirmacion-card">
          <div className="confirmacion-icon">
            <i className="ti ti-thumb-up"></i>
          </div>

          <h1 className="confirmacion-title">Pago exitoso</h1>
          <p className="confirmacion-folio">Folio de compra {folio}</p>

          <div className="confirmacion-detalle">
            <div className="confirmacion-detalle-row">
              <span>Evento</span>
              <span>{evento}</span>
            </div>
            <div className="confirmacion-detalle-row">
              <span>Boletos</span>
              <span>{boletos}</span>
            </div>
            <div className="confirmacion-detalle-row">
              <span>Total pagado</span>
              <span>
                ${total.toLocaleString("es-MX", { minimumFractionDigits: 2 })}
              </span>
            </div>
          </div>
        </div>

        <button
          className="confirmacion-btn"
          onClick={() => navigate("/mis-boletos")}
        >
          Ver mis boletos
        </button>
        <button className="confirmacion-btn" onClick={handleDescargarComprobante}>
          Descargar comprobante
        </button>
      </div>
    </div>
  );
}