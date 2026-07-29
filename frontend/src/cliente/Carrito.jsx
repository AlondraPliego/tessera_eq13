import { useState, useEffect, useCallback, useMemo } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import logo from "../assets/img/logo.png";
import ticketThumb from "../assets/img/modalejemplo.png";
import { procesarPago, liberarReserva } from "../services/cartService";
import "./Carrito.css";

const CARGO_SERVICIO = 100.0;

export default function Carrito() {
  const navigate = useNavigate();
  const location = useLocation();

  const datosEvento = location.state || null;
  const [items, setItems] = useState(datosEvento?.items || []);
  const [segundosRestantes, setSegundosRestantes] = useState(0);
  const [procesandoPago, setProcesandoPago] = useState(false);
  const [reservaExpirada, setReservaExpirada] = useState(false);
  const [errorPago, setErrorPago] = useState(null);

  const expiraEnMasProximo = useMemo(() => {
    if (items.length === 0) return null;
    return items.reduce((min, item) => {
      const t = new Date(item.expiraEn).getTime();
      return min === null || t < min ? t : min;
    }, null);
  }, [items]);

  useEffect(() => {
    if (!expiraEnMasProximo) return;
    const actualizar = () => {
      const restantes = Math.max(0, Math.floor((expiraEnMasProximo - Date.now()) / 1000));
      setSegundosRestantes(restantes);
      if (restantes === 0) setReservaExpirada(true);
    };
    actualizar();
    const intervalo = setInterval(actualizar, 1000);
    return () => clearInterval(intervalo);
  }, [expiraEnMasProximo]);

  const formatearTiempo = (segundos) => {
    const min = Math.floor(segundos / 60);
    const seg = segundos % 60;
    return `${String(min).padStart(2, "0")}:${String(seg).padStart(2, "0")}`;
  };

  const handleEliminar = (reservaId) => {
    setItems((prev) => prev.filter((item) => item.reservaId !== reservaId));
    liberarReserva(reservaId).catch((err) =>
      console.error("Error al liberar la reserva:", err)
    );
  };

  const subtotal = items.reduce((acc, item) => acc + item.precio * item.cantidad, 0);
  const total = subtotal + (items.length > 0 ? CARGO_SERVICIO : 0);

  const handleContinuarPago = useCallback(() => {
    if (items.length === 0) return;
    setErrorPago(null);
    setProcesandoPago(true);

    procesarPago(items)
      .then(({ folio, total: totalPagado }) => {
        navigate(`/confirmacion/${folio}`, {
          state: {
            evento: datosEvento?.nombreEvento,
            boletos: items.length,
            total: totalPagado,
          },
        });
      })
      .catch((err) => {
        console.error("Error al procesar el pago:", err);
        setErrorPago(
          err.response?.data?.mensaje ||
            "No se pudo completar el pago. Puede que tu reserva haya expirado."
        );
        setProcesandoPago(false);
      });
  }, [items, datosEvento, navigate]);

  if (!datosEvento) {
    return (
      <div className="carrito-page">
        <p className="carrito-vacio">
          Tu carrito está vacío.{" "}
          <Link to="/">Vuelve al inicio para elegir un evento.</Link>
        </p>
      </div>
    );
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
                <div key={item.reservaId} className="carrito-item">
                  <img
                    src={datosEvento.imagenUrl || ticketThumb}
                    alt={datosEvento.nombreEvento}
                    className="carrito-item-img"
                  />
                  <div className="carrito-item-info">
                    <p className="carrito-item-nombre">
                      {datosEvento.nombreEvento}{" "}
                      <span className="carrito-item-dot">●</span> {item.nombre}
                    </p>
                    <p className="carrito-item-detalle">
                      {item.cantidad} boleto(s) · {datosEvento.fecha} {datosEvento.hora}
                    </p>
                  </div>
                  <div className="carrito-item-right">
                    <span className="carrito-item-precio">
                      $
                      {(item.precio * item.cantidad).toLocaleString("es-MX", {
                        minimumFractionDigits: 2,
                      })}
                    </span>
                    <button
                      className="carrito-item-eliminar"
                      onClick={() => handleEliminar(item.reservaId)}
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
                {(items.length > 0 ? CARGO_SERVICIO : 0).toLocaleString("es-MX", {
                  minimumFractionDigits: 2,
                })}
              </span>
            </div>
          </div>

          <div className="carrito-total-final">
            <span>Total</span>
            <span>${total.toLocaleString("es-MX", { minimumFractionDigits: 2 })}</span>
          </div>

          {errorPago && <p className="asientos-error">{errorPago}</p>}

          <button
            className="carrito-pagar-btn"
            disabled={items.length === 0 || reservaExpirada}
            onClick={handleContinuarPago}
          >
            Continuar al pago
          </button>
        </div>
      </div>

      {procesandoPago && (
        <div className="carrito-modal-overlay">
          <div className="carrito-modal-procesando">
            <div className="carrito-spinner"></div>
            <p className="carrito-modal-title">Tu pago está siendo procesado</p>
            <p className="carrito-modal-sub">Por favor espera, no cierres esta ventana...</p>
          </div>
        </div>
      )}

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