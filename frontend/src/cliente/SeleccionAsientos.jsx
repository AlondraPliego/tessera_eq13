import { useState, useEffect } from "react";
import { Link, useParams, useSearchParams, useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import eventoPlaceholder from "../assets/img/modalejemplo.png";
import mapaPlaceholder from "../assets/img/mapaejemplo.png";
import { getEventoParaAsientos } from "../services/venueService";
import { crearReserva, liberarReserva } from "../services/cartService";
import "./SeleccionAsientos.css";


export default function SeleccionAsientos() {
  const { eventoId } = useParams();
  const [searchParams] = useSearchParams();
  const fechaId = searchParams.get("fecha");
  const navigate = useNavigate();

  const [datos, setDatos] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [reservando, setReservando] = useState(null);
  const [error, setError] = useState(null);
  const [seleccionados, setSeleccionados] = useState([]);

  useEffect(() => {
    async function cargarDatos() {
      setCargando(true);
      try {
        const data = await getEventoParaAsientos(eventoId, fechaId);
        setDatos(data);
      } catch (err) {
        console.error("Error al cargar la selección de asientos:", err);
        setError("No se pudo cargar el evento. Intenta de nuevo.");
      } finally {
        setCargando(false);
      }
    }
    cargarDatos();
  }, [eventoId, fechaId]);

  useEffect(() => {
    return () => {
      seleccionados.forEach((item) => {
        liberarReserva(item.reservaId).catch(() => {});
      });
    };
  }, []);

  const handleSeleccionarSeccion = async (seccion) => {
    setError(null);
    setReservando(seccion.id);
    try {
      const reserva = await crearReserva(seccion.id, 1);
      setSeleccionados((prev) => [
        ...prev,
        {
          reservaId: reserva.id,
          boletoEventoId: seccion.id,
          nombre: seccion.nombre,
          cantidad: reserva.cantidad,
          precio: reserva.precioUnitario,
          expiraEn: reserva.expiraEn,
        },
      ]);
    } catch (err) {
      console.error("No se pudo reservar la sección:", err);
      setError(
        err.response?.data?.mensaje ||
          "Ya no hay boletos disponibles en esa sección. Elige otra."
      );
    } finally {
      setReservando(null);
    }
  };

  const handleQuitarSeleccion = async (reservaId) => {
    setSeleccionados((prev) => prev.filter((item) => item.reservaId !== reservaId));
    try {
      await liberarReserva(reservaId);
    } catch (err) {
      console.error("Error al liberar la reserva:", err);
    }
  };

  const handleComprar = () => {
    if (seleccionados.length === 0) return;
    navigate("/carrito", {
      state: {
        eventoId,
        nombreEvento: datos.nombreEvento,
        imagenUrl: datos.imagenUrl,
        fecha: datos.fecha,
        hora: datos.hora,
        items: seleccionados,
      },
    });
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

      {error && <p className="asientos-error">{error}</p>}

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
            {/* aqui falta el seatmap */}
          </div>
        </div>

        <div className="asientos-secciones-side">
          <h2 className="asientos-secciones-title">Secciones disponibles</h2>

          <div className="asientos-secciones-list">
            {datos.secciones.map((seccion) => (
              <div key={seccion.id} className="seccion-card">
                <p className="seccion-nombre">{seccion.nombre}</p>
                <p className="seccion-tipo">
                  <span className="seccion-dot"></span> {seccion.tipo} ·{" "}
                  {seccion.disponibles} disponibles
                </p>
                <p className="seccion-precio">
                  ${seccion.precio.toLocaleString("es-MX", { minimumFractionDigits: 2 })}{" "}
                  cada uno
                </p>
                <button
                  className="btn-navy"
                  disabled={seccion.disponibles === 0 || reservando === seccion.id}
                  onClick={() => handleSeleccionarSeccion(seccion)}
                >
                  {reservando === seccion.id
                    ? "Reservando..."
                    : seccion.disponibles === 0
                    ? "Agotado"
                    : "Seleccionar"}
                </button>
              </div>
            ))}
          </div>

          {seleccionados.length > 0 && (
            <div className="asientos-seleccionados-card">
              <h3 className="asientos-seleccionados-title">Asientos Seleccionados</h3>

              <div className="asientos-seleccionados-list">
                {seleccionados.map((item) => (
                  <div key={item.reservaId} className="asiento-seleccionado-row">
                    <span className="seccion-dot"></span>
                    <span className="asiento-seleccionado-etiqueta">{item.nombre}</span>
                    <span className="asiento-seleccionado-precio">
                      ${item.precio.toLocaleString("es-MX", { minimumFractionDigits: 2 })}
                    </span>
                    <button
                      className="asiento-seleccionado-quitar"
                      onClick={() => handleQuitarSeleccion(item.reservaId)}
                      aria-label="Quitar"
                    >
                      <i className="ti ti-x"></i>
                    </button>
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
