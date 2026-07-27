import { useState, useEffect } from "react";
import { useParams, useSearchParams, useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import eventoPlaceholder from "../assets/img/modalejemplo.png";
import mapaPlaceholder from "../assets/img/mapaejemplo.png";
import { getEventoParaAsientos } from "../services/venueService";
import "./SeleccionAsientos.css";

export default function SeleccionAsientos() {
  const { eventoId } = useParams();
  const [searchParams] = useSearchParams();
  const fechaId = searchParams.get("fecha");
  const navigate = useNavigate();

  const [datos, setDatos] = useState(null);
  const [cargando, setCargando] = useState(true);

  // --- Popup de "detalles del asiento" (hotspot fijo mientras no hay mapa interactivo real) ---
  const [mostrarPopup, setMostrarPopup] = useState(false);

  const asientoEjemplo = {
    seccion: "GCE-015",
    fila: "F",
    asiento: "20",
    precio: 1463.25,
  };

  // --- Lista de asientos ya seleccionados (panel derecho) ---
  const [seleccionados, setSeleccionados] = useState([]);

  useEffect(() => {
    async function cargarDatos() {
      setCargando(true);
      try {
        const data = await getEventoParaAsientos(eventoId, fechaId);
        setDatos(data);
      } catch (error) {
        console.error("Error al cargar la selección de asientos:", error);
      } finally {
        setCargando(false);
      }
    }
    cargarDatos();
  }, [eventoId, fechaId]);

  const handleSeleccionarAsiento = () => {
    const nuevoAsiento = {
      id: Date.now(),
      etiqueta: `${asientoEjemplo.seccion.split("-")[0]}-13,15,16,17`,
      precio: 5500.0, 
    };

    setSeleccionados((prev) => [...prev, nuevoAsiento]);
    setMostrarPopup(false);
  };

  const handleComprar = () => {
    if (seleccionados.length === 0) return;
    navigate(`/carrito?evento=${eventoId}&asientos=${seleccionados.length}`);
  };

  if (cargando || !datos) {
    return <p className="asientos-loading">Cargando evento...</p>;
  }

  return (
    <div className="asientos-page">
      <div className="asientos-topbar">
        <Link to="/" className="home-logo">
  <img src={logo} alt="Tessera" className="home-logo-img" />
  Tessera
</Link>
        <Link to="/perfil" className="home-login-btn">
  <i className="ti ti-user"></i>
  Usuario
</Link>
      </div>

      <div className="asientos-evento-info">
        <img
          src={datos.imagenUrl || eventoPlaceholder}
          alt={datos.nombreEvento}
          className="asientos-evento-img"
        />
        <div>
          <h1 className="asientos-evento-nombre">{datos.nombreEvento}</h1>
          <p className="asientos-evento-fecha">
            {datos.fecha} | {datos.hora}
          </p>
          <p className="asientos-evento-recinto">{datos.recinto}</p>
        </div>
      </div>

      <div className="asientos-content">
        <div className="asientos-mapa-side">
          <div className="asientos-leyenda">
            <span className="leyenda-item">
              <span className="leyenda-dot disponible"></span> Disponible
            </span>
            <span className="leyenda-item">
              <span className="leyenda-dot seleccionado"></span> Seleccionado
            </span>
            <span className="leyenda-item">
              <span className="leyenda-dot ocupado"></span> Ocupado
            </span>
          </div>

          <div className="asientos-mapa">
            <img src={datos.mapaUrl || mapaPlaceholder} alt="Mapa del recinto" />

            {/* Hotspot fijo que simula un asiento libre — reemplazar cuando el mapa sea interactivo real */}
            <div
              className="asiento-hotspot"
              onMouseEnter={() => setMostrarPopup(true)}
              onMouseLeave={() => setMostrarPopup(false)}
            >
              {mostrarPopup && (
                <div
                  className="asiento-popup"
                  onMouseEnter={() => setMostrarPopup(true)}
                  onMouseLeave={() => setMostrarPopup(false)}
                >
                  <p className="asiento-popup-title">Detalles del asiento</p>
                  <div className="asiento-popup-card">
                    <div className="asiento-popup-info">
                      <div>
                        <span className="asiento-popup-label">Sección</span>
                        <span className="asiento-popup-value">
                          {asientoEjemplo.seccion}
                        </span>
                      </div>
                      <div>
                        <span className="asiento-popup-label">Fila</span>
                        <span className="asiento-popup-value">{asientoEjemplo.fila}</span>
                      </div>
                      <div>
                        <span className="asiento-popup-label">Asiento</span>
                        <span className="asiento-popup-value">
                          {asientoEjemplo.asiento}
                        </span>
                      </div>
                    </div>
                    <p className="asiento-popup-precio">
                      Precio: $
                      {asientoEjemplo.precio.toLocaleString("es-MX", {
                        minimumFractionDigits: 2,
                      })}
                    </p>
                    <div className="asiento-popup-buttons">
                      <button
                        className="asiento-popup-btn cerrar"
                        onClick={() => setMostrarPopup(false)}
                      >
                        Cerrar
                      </button>
                      <button
                        className="asiento-popup-btn seleccionar"
                        onClick={handleSeleccionarAsiento}
                      >
                        Seleccionar
                      </button>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="asientos-secciones-side">
          <h2 className="asientos-secciones-title">Secciones disponibles</h2>

          <div className="asientos-secciones-list">
            {datos.secciones.map((seccion) => (
              <div key={seccion.id} className="seccion-card">
                <p className="seccion-nombre">{seccion.nombre}</p>
                <p className="seccion-tipo">
                  <span className="seccion-dot"></span> {seccion.tipo}
                </p>
                <p className="seccion-precio">
                  $
                  {seccion.precio.toLocaleString("es-MX", {
                    minimumFractionDigits: 2,
                  })}{" "}
                  cada uno
                </p>
              </div>
            ))}
          </div>

          {seleccionados.length > 0 && (
            <div className="asientos-seleccionados-card">
              <h3 className="asientos-seleccionados-title">Asientos Seleccionados</h3>

              <div className="asientos-seleccionados-list">
                {seleccionados.map((asiento) => (
                  <div key={asiento.id} className="asiento-seleccionado-row">
                    <span className="seccion-dot"></span>
                    <span className="asiento-seleccionado-etiqueta">
                      {asiento.etiqueta}
                    </span>
                    <span className="asiento-seleccionado-precio">
                      $
                      {asiento.precio.toLocaleString("es-MX", {
                        minimumFractionDigits: 2,
                      })}
                    </span>
                  </div>
                ))}
              </div>

              <button className="btn-navy asientos-comprar-btn" onClick={handleComprar}>
                Comprar
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}