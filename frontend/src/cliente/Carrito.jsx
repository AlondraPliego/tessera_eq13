import { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import ticketThumb from "../assets/img/modalejemplo.png";
import { getCarrito, procesarPago } from "../services/cartService";
import "./Carrito.css";

export default function Carrito() {
  const navigate = useNavigate();

  const [items, setItems] = useState([]);
  const [cargoServicio, setCargoServicio] = useState(0);
  const [segundosRestantes, setSegundosRestantes] = useState(0);
  const [cargando, setCargando] = useState(true);
  const [procesandoPago, setProcesandoPago] = useState(false); // 👈 controla el modal propio
  const [reservaExpirada, setReservaExpirada] = useState(false);

  useEffect(() => {
    async function cargarCarrito() {
      setCargando(true);
      try {
        const data = await getCarrito();
        setItems(data.items);
        setCargoServicio(data.cargoServicio);
        setSegundosRestantes(data.tiempoExpiraSegundos);
      } catch (error) {
        console.error("Error al cargar el carrito:", error);
      } finally {
        setCargando(false);
      }
    }
    cargarCarrito();
  }, []);

  // --- Temporizador de reserva ---
  useEffect(() => {
    if (segundosRestantes <= 0) return;

    const intervalo = setInterval(() => {
      setSegundosRestantes((prev) => {
        if (prev <= 1) {
          clearInterval(intervalo);
          setReservaExpirada(true);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(intervalo);
  }, [segundosRestantes === 0]); // eslint-disable-line react-hooks/exhaustive-deps

  const formatearTiempo = (segundos) => {
    const min = Math.floor(segundos / 60);
    const seg = segundos % 60;
    return `${String(min).padStart(2, "0")}:${String(seg).padStart(2, "0")}`;
  };

  const handleEliminar = (id) => {
    setItems((prev) => prev.filter((item) => item.id !== id));
  };

  const subtotal = items.reduce((acc, item) => acc + item.precio, 0);
  const total = subtotal + (items.length > 0 ? cargoServicio : 0);

  const handleContinuarPago = useCallback(() => {
    if (items.length === 0) return;
    setProcesandoPago(true);

    // Simula el tiempo de procesamiento (7 segundos), luego pasa a confirmación real
    setTimeout(async () => {
      try {
        const idsItems = items.map((item) => item.id);
        const { folio } = await procesarPago(idsItems);
        navigate(`/confirmacion/${folio}`, {
          state: { evento: items[0]?.evento, boletos: items.length, total },
        });
      } catch (error) {
        console.error("Error al procesar el pago:", error);
        setProcesandoPago(false);
      }
    }, 7000);
  }, [items, total, navigate]);

  if (cargando) {
    return <p className="carrito-loading">Cargando carrito...</p>;
  }

  return (
    <div className="carrito-page">
      <div className="carrito-topbar">
        <Link to="/" className="home-logo">
  <img src={logo} alt="Tessera" className="home-logo-img" />
  Tessera
</Link>
        <Link to="/perfil" className="home-login-btn">
  <i className="ti ti-user"></i>
  Usuario
</Link>
      </div>

      <div className="carrito-content">
        <div className="carrito-items-side">
          <h1 className="carrito-title">Carrito de compras</h1>

          {items.length === 0 ? (
            <p className="carrito-vacio">Tu carrito está vacío.</p>
          ) : (
            <div className="carrito-items-list">
              {items.map((item) => (
                <div key={item.id} className="carrito-item">
                  <img
                    src={item.imagenUrl || ticketThumb}
                    alt={item.evento}
                    className="carrito-item-img"
                  />
                  <div className="carrito-item-info">
                    <p className="carrito-item-nombre">
                      {item.evento} <span className="carrito-item-dot">●</span> {item.tipo}
                    </p>
                    <p className="carrito-item-detalle">
                      Sección {item.seccion} - Asiento {item.asiento} - {item.fecha}
                    </p>
                  </div>
                  <div className="carrito-item-right">
                    <span className="carrito-item-precio">
                      $
                      {item.precio.toLocaleString("es-MX", {
                        minimumFractionDigits: 2,
                      })}
                    </span>
                    <button
                      className="carrito-item-eliminar"
                      onClick={() => handleEliminar(item.id)}
                      aria-label="Eliminar boleto"
                    >
                      <i className="ti ti-trash"></i>
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="carrito-resumen-side">
          <div className="carrito-timer">
            <i className="ti ti-clock"></i>
            <span>
              Tu reserva expira en <strong>{formatearTiempo(segundosRestantes)}</strong>
            </span>
          </div>

          <div className="carrito-totales">
            <div className="carrito-total-row">
              <span>Subtotal</span>
              <span>
                ${subtotal.toLocaleString("es-MX", { minimumFractionDigits: 2 })}
              </span>
            </div>
            <div className="carrito-total-row">
              <span>Cargo por servicio</span>
              <span>
                $
                {(items.length > 0 ? cargoServicio : 0).toLocaleString("es-MX", {
                  minimumFractionDigits: 2,
                })}
              </span>
            </div>
          </div>

          <div className="carrito-total-final">
            <span>Total</span>
            <span>${total.toLocaleString("es-MX", { minimumFractionDigits: 2 })}</span>
          </div>

          <button
            className="carrito-pagar-btn"
            disabled={items.length === 0}
            onClick={handleContinuarPago}
          >
            Continuar al pago
          </button>
        </div>
      </div>

      {/* Modal propio: pago siendo procesado */}
      {procesandoPago && (
        <div className="carrito-modal-overlay">
          <div className="carrito-modal-procesando">
            <div className="carrito-spinner"></div>
            <p className="carrito-modal-title">Tu pago está siendo procesado</p>
            <p className="carrito-modal-sub">Por favor espera, no cierres esta ventana...</p>
          </div>
        </div>
      )}

      {/* Modal propio: reserva expirada */}
      {reservaExpirada && (
        <div className="carrito-modal-overlay">
          <div className="carrito-modal-expirado">
            <i className="ti ti-alert-triangle carrito-modal-icon"></i>
            <p className="carrito-modal-title">Tu reserva expiró</p>
            <p className="carrito-modal-sub">
              El tiempo para completar la compra ha terminado.
            </p>
            <button className="carrito-modal-btn" onClick={() => navigate("/")}>
              Entendido
            </button>
          </div>
        </div>
      )}
    </div>
  );
}