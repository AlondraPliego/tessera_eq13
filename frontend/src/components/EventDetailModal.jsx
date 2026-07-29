import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import placeholderImg from "../assets/img/modalejemplo.png";
import { getDetalleEvento } from "../services/eventService";
import "./EventDetailModal.css";
import { resolverUrlImagen } from "../services/api";

export default function EventDetailModal({ eventoId, onClose }) {
  const [detalle, setDetalle] = useState(null);
  const [cargando, setCargando] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    async function cargarDetalle() {
      setCargando(true);
      try {
        const data = await getDetalleEvento(eventoId);
        setDetalle(data);
      } catch (error) {
        console.error("Error al cargar el detalle del evento:", error);
      } finally {
        setCargando(false);
      }
    }

    cargarDetalle();
  }, [eventoId]);

  const handleSeleccionarFecha = (fechaId) => {
    navigate(`/evento/${eventoId}/asientos?fecha=${fechaId}`);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="event-modal" onClick={(e) => e.stopPropagation()}>
        <button className="modal-close" onClick={onClose} aria-label="Cerrar">
          <i className="ti ti-x"></i>
        </button>

        {cargando || !detalle ? (
          <p className="modal-loading">Cargando evento...</p>
        ) : (
          <div className="modal-body">
            <div className="modal-image">
              <img
  src={detalle.imagenUrl ? resolverUrlImagen(detalle.imagenUrl) : placeholderImg}
  alt={detalle.nombreEvento}
/>
            </div>

            <div className="modal-info">
              <h2 className="modal-title">{detalle.nombreEvento}</h2>
              <p className="modal-description">{detalle.descripcion}</p>

              <h3 className="modal-subtitle">Fechas disponibles</h3>

              <div className="modal-dates">
                {detalle.fechas.map((fecha) => (
                  <button
                    key={fecha.id}
                    className="modal-date-card"
                    onClick={() => handleSeleccionarFecha(fecha.id)}
                  >
                    <div className="modal-date-badge">
                      <span className="modal-date-mes">{fecha.mes}</span>
                      <span className="modal-date-dia">{fecha.dia}</span>
                    </div>
                    <div className="modal-date-text">
                      <p>
                        {fecha.diaSemana} • {fecha.hora} • {fecha.recinto}
                      </p>
                      <p className="modal-date-funcion">{fecha.nombreFuncion}</p>
                    </div>
                  </button>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}