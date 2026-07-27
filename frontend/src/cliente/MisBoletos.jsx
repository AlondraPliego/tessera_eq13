import { useState, useEffect } from "react";
import logo from "../assets/img/logo.png";
import posterPlaceholder from "../assets/img/modalejemplo.png";
import { getBoletosActivos, getHistorialEventos } from "../services/ticketService";
import "./MisBoletos.css";
import { Link } from "react-router-dom";
export default function MisBoletos() {
  const [boletos, setBoletos] = useState([]);
  const [historial, setHistorial] = useState([]);
  const [indiceActivo, setIndiceActivo] = useState(0);
  const [cargando, setCargando] = useState(true);

  useEffect(() => {
    async function cargarDatos() {
      setCargando(true);
      try {
        const [activos, hist] = await Promise.all([
          getBoletosActivos(),
          getHistorialEventos(),
        ]);
        setBoletos(activos);
        setHistorial(hist);
      } catch (error) {
        console.error("Error al cargar boletos:", error);
      } finally {
        setCargando(false);
      }
    }
    cargarDatos();
  }, []);

  const boletoActual = boletos[indiceActivo];

  const irAnterior = () => {
    setIndiceActivo((prev) => (prev === 0 ? boletos.length - 1 : prev - 1));
  };

  const irSiguiente = () => {
    setIndiceActivo((prev) => (prev === boletos.length - 1 ? 0 : prev + 1));
  };

  if (cargando) {
    return <p className="misboletos-loading">Cargando tus boletos...</p>;
  }

  return (
    <div className="misboletos-page">
      <div className="misboletos-topbar">
        <Link to="/" className="home-logo">
  <img src={logo} alt="Tessera" className="home-logo-img" />
  Tessera
</Link>

        <div className="misboletos-search">
          <input type="text" placeholder="Busca artista, evento o recinto" />
          <i className="ti ti-search"></i>
        </div>

        <Link to="/perfil" className="home-login-btn">
  <i className="ti ti-user"></i>
  Usuario
</Link>
      </div>

      <h1 className="misboletos-title">Boletos activos</h1>

      {boletos.length === 0 ? (
        <p className="misboletos-vacio">No tienes boletos activos por el momento.</p>
      ) : (
        <div className="misboletos-carousel">
          <button
            className="carousel-arrow"
            onClick={irAnterior}
            disabled={boletos.length <= 1}
          >
            <i className="ti ti-chevron-left"></i>
          </button>

          <div className="misboletos-carousel-content">
            <img
              src={boletoActual.posterUrl || posterPlaceholder}
              alt={boletoActual.tour}
              className="misboletos-poster"
            />

            <div className="misboletos-detalle-card">
              <div className="misboletos-detalle-info">
                <p className="misboletos-detalle-nombre">
                  {boletoActual.evento} <span className="dot">●</span> {boletoActual.tipo}
                </p>
                <p className="misboletos-detalle-sub">
                  Sección {boletoActual.seccion} - Asiento {boletoActual.asiento} -{" "}
                  {boletoActual.fecha}
                </p>
                <a href="#" className="misboletos-ver-qr">
                  Ver código QR
                </a>
              </div>
              <i className="ti ti-qrcode misboletos-qr-icon" aria-hidden="true"></i>
            </div>
          </div>

          <button
            className="carousel-arrow"
            onClick={irSiguiente}
            disabled={boletos.length <= 1}
          >
            <i className="ti ti-chevron-right"></i>
          </button>
        </div>
      )}

      <h2 className="misboletos-historial-title">Historial de eventos</h2>

      {historial.length === 0 ? (
        <p className="misboletos-vacio">Aún no tienes eventos en tu historial.</p>
      ) : (
        <div className="misboletos-historial-row">
          <button className="carousel-arrow small">
            <i className="ti ti-chevron-left"></i>
          </button>

          <div className="misboletos-historial-list">
            {historial.map((h) => (
              <div key={h.id} className="historial-card">
                <div>
                  <p className="historial-nombre">{h.evento}</p>
                  <p className="historial-sub">
                    Finalizado -- {h.cantidadBoletos} boletos
                  </p>
                </div>
                <i className="ti ti-circle-check historial-check" aria-hidden="true"></i>
              </div>
            ))}
          </div>

          <button className="carousel-arrow small">
            <i className="ti ti-chevron-right"></i>
          </button>
        </div>
      )}
    </div>
  );
}