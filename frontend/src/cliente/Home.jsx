import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import logo from "../assets/img/logo.png";
import bannerImg from "../assets/img/destacado.png";
import { getEventosRecomendados } from "../services/eventService";
import EventCard from "../components/EventCard";
import Pagination from "../components/Pagination";
import EventDetailModal from "../components/EventDetailModal"; 
import { useAuth } from "../AuthContext";
import "./Home.css";

const CATEGORIAS = ["Todos", "Conciertos", "Festivales", "Deportes", "Teatro y Musicales"];

export default function Home() {
  const { user } = useAuth();
  const [categoriaActiva, setCategoriaActiva] = useState("Todos");
  const [paginaActual, setPaginaActual] = useState(1);
  const [totalPaginas, setTotalPaginas] = useState(1);
  const [eventos, setEventos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [eventoSeleccionado, setEventoSeleccionado] = useState(null); 

  useEffect(() => {
    async function cargarEventos() {
      setCargando(true);
      try {
        const data = await getEventosRecomendados({
          categoria: categoriaActiva,
          pagina: paginaActual,
        });
        setEventos(data.eventos);
        setTotalPaginas(data.totalPaginas);
      } catch (error) {
        console.error("Error al cargar eventos:", error);
      } finally {
        setCargando(false);
      }
    }

    cargarEventos();
  }, [categoriaActiva, paginaActual]);

  const handleCambiarCategoria = (categoria) => {
    setCategoriaActiva(categoria);
    setPaginaActual(1);
  };

  return (
    <div className="home-page">
      
      <nav className="home-navbar">
        <span className="home-logo">
          <img src={logo} alt="Tessera" className="home-logo-img" />
          Tessera
        </span>

        <div className="home-search">
          <i className="ti ti-search"></i>
          <input type="text" placeholder="Busca artista, evento o recinto" />
        </div>

        <div className="home-nav-links">
          <Link to="/carrito">Compras</Link>
          <Link to="/mis-boletos">Historial</Link>
          <Link to="/eventos">Eventos</Link>
          {user ? (
            <Link
              to={
                user.rol === "ROLE_EMPRESA"
                  ? "/empresa/dashboard"
                  : user.rol === "ROLE_ADMIN"
                  ? "/admin/dashboard"
                  : "/perfil"
              }
              className="home-login-btn"
            >
              <i className="ti ti-user"></i> {user.nombre || user.email}
            </Link>
          ) : (
            <Link to="/login" className="home-login-btn">
              Iniciar sesión
            </Link>
          )}
        </div>
      </nav>

      <div className="home-hero">
        <h1 className="home-hero-title">Evento destacado del mes</h1>
        <div className="home-hero-banner">
          <img src={bannerImg} alt="Evento destacado" />
        </div>
      </div>

      <div className="home-content">
        <h2 className="home-section-title">Eventos recomendados</h2>

        <div className="home-filters">
          {CATEGORIAS.map((categoria) => (
            <button
              key={categoria}
              className={`filter-pill ${categoriaActiva === categoria ? "active" : ""}`}
              onClick={() => handleCambiarCategoria(categoria)}
            >
              {categoria}
            </button>
          ))}
        </div>

        {cargando ? (
          <p className="home-loading">Cargando eventos...</p>
        ) : (
          <div className="home-events-grid">
            {eventos.map((evento) => (
              <div
                key={evento.id}
                onClick={() => setEventoSeleccionado(evento.id)}
                style={{ cursor: "pointer" }}
              >
                <EventCard evento={evento} />
              </div>
            ))}
          </div>
        )}

        <Pagination
          paginaActual={paginaActual}
          totalPaginas={totalPaginas}
          onCambiarPagina={setPaginaActual}
        />
      </div>

      {eventoSeleccionado && (
        <EventDetailModal
          eventoId={eventoSeleccionado}
          onClose={() => setEventoSeleccionado(null)}
        />
      )}
    </div>
  );
}