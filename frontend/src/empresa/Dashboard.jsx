import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import { getMisEventos } from "../services/organizerService";
import PerfilComercialDropdown from "./PerfilComercialDropdown";
import Pagination from "../components/Pagination";
import "./Dashboard.css";

export default function Dashboard() {
  const navigate = useNavigate();
  const [dropdownAbierto, setDropdownAbierto] = useState(false);
  const [paginaActual, setPaginaActual] = useState(1);
  const [totalPaginas, setTotalPaginas] = useState(1);
  const [metricas, setMetricas] = useState(null);
  const [eventos, setEventos] = useState([]);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    async function cargar() {
      setCargando(true);
      try {
        const data = await getMisEventos({ pagina: paginaActual });
        setMetricas(data.metricas);
        setEventos(data.eventos);
        setTotalPaginas(data.totalPaginas);
      } catch (error) {
        console.error("Error al cargar el dashboard:", error);
      } finally {
        setCargando(false);
      }
    }
    cargar();
  }, [paginaActual]);

  return (
    <div className="dashboard-page">
      <div className="dashboard-topbar">
        <span className="dashboard-logo" onClick={() => navigate("/")}>
          Tessera
        </span>

        <div className="dashboard-nav-right">
          <span className="dashboard-nav-link">Mis eventos</span>
          <button
            className="dashboard-recintos-btn"
            onClick={() => navigate("/empresa/recintos")}
          >
            Mis recintos
          </button>
          <button
            className="dashboard-crear-btn"
            onClick={() => navigate("/empresa/evento/nuevo")}
          >
            + Crear evento
          </button>

          <div className="dashboard-dropdown-wrapper">
            <button
              className="dashboard-usuario-btn"
              onClick={() => setDropdownAbierto((prev) => !prev)}
            >
              <i className="ti ti-user"></i>
              Usuario
              <i className="ti ti-chevron-down"></i>
            </button>

            {dropdownAbierto && (
              <PerfilComercialDropdown onClose={() => setDropdownAbierto(false)} />
            )}
          </div>
        </div>
      </div>

      <div className="dashboard-header">
        <h1 className="dashboard-title">Dashboard del organizador</h1>
        <p className="dashboard-subtitle">Resumen de eventos y desempeño de tus eventos.</p>
      </div>

      {cargando || !metricas ? (
        <p className="dashboard-loading">Cargando...</p>
      ) : (
        <>
          <div className="dashboard-metrics">
            <div className="metric-card">
              <span className="metric-label">Ingreso potencial</span>
              <span className="metric-value">
                ${metricas.gananciasTotales.toLocaleString("es-MX")}
              </span>
            </div>
            <div className="metric-card">
              <span className="metric-label">Boletos vendidos</span>
              <span className="metric-value">
                {metricas.boletosVendidos != null ? metricas.boletosVendidos.toLocaleString("es-MX") : "—"}
              </span>
            </div>
            <div className="metric-card">
              <span className="metric-label">Eventos activos</span>
              <span className="metric-value">{metricas.eventosActivos}</span>
            </div>
            <div className="metric-card">
              <span className="metric-label">Ocupación</span>
              <span className="metric-value">{metricas.ocupacion != null ? `${metricas.ocupacion}%` : "—"}</span>
            </div>
          </div>

          <div className="dashboard-table-card">
            <table className="dashboard-table">
              <thead>
                <tr>
                  <th>Evento</th>
                  <th>Fecha</th>
                  <th>Boletos disponibles</th>
                  <th>Ingreso potencial</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                {eventos.map((evento) => (
                  <tr
                    key={evento.id}
                    className="dashboard-row-clickable"
                    onClick={() => navigate(`/empresa/evento/${evento.id}/editar`)}
                  >
                    <td>{evento.nombre}</td>
                    <td>{evento.fecha}</td>
                    <td>{evento.boletosTotales != null ? evento.boletosTotales : "—"}</td>
                    <td>{evento.ganancia != null ? `$${evento.ganancia.toLocaleString("es-MX")}` : "—"}</td>
                    <td>{evento.estado}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            <Pagination
              paginaActual={paginaActual}
              totalPaginas={totalPaginas}
              onCambiarPagina={setPaginaActual}
            />
          </div>
        </>
      )}
    </div>
  );
}
