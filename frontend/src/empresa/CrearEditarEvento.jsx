import { useState, useEffect } from "react";
import { subirImagen } from "../services/uploadService";
import { useParams, useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import { useValidacion } from "../hooks/useValidacion";
import { requerido } from "../hooks/validadores";
import FormField from "../components/FormField";
import {
  getEventoParaEditar,
  crearEvento,
  cambiarEstadoEvento,
} from "../services/organizerService";
import { getMisRecintos, getZonasDeRecinto } from "../services/recintoService";
import "./CrearEditarEvento.css";

const ESTADOS = ["PROGRAMADO", "AGOTADO", "CANCELADO", "FINALIZADO"];

export default function CrearEditarEvento() {
  const { eventoId } = useParams();
  const esEdicion = Boolean(eventoId);
  const navigate = useNavigate();
  const [cargando, setCargando] = useState(true);
  const [guardando, setGuardando] = useState(false);

  // --- Modo edición: el backend real solo permite cambiar el estado del evento ---
  const [eventoExistente, setEventoExistente] = useState(null);
  const [estadoSeleccionado, setEstadoSeleccionado] = useState("PROGRAMADO");

  const { form, errores, handleChange, validar } = useValidacion(
    {
      nombreEvento: "",
      descripcion: "",
      ciudad: "",
      recintoId: "",
      fecha: "",
      hora: "",
    },
    {
      nombreEvento: [requerido],
      ciudad: [requerido],
      recintoId: [requerido],
      fecha: [requerido],
      hora: [requerido],
    }
  );

  const [flyerUrl, setFlyerUrl] = useState(null);
  const [subiendoImagen, setSubiendoImagen] = useState(false);

  const [misRecintos, setMisRecintos] = useState([]);
  const [zonasDelRecinto, setZonasDelRecinto] = useState([]);

  const [nuevaZona, setNuevaZona] = useState({ zonaId: "", precio: "", asientos: "" });
  const [zonas, setZonas] = useState([]); // zonas ya agregadas al evento nuevo

  // --- Carga inicial: recintos de la empresa (solo aplica al crear) ---
  useEffect(() => {
    if (esEdicion) return;
    (async () => {
      try {
        const recintos = await getMisRecintos();
        setMisRecintos(recintos);
      } catch (error) {
        console.error("Error al cargar tus recintos:", error);
      }
    })();
  }, [esEdicion]);

  // --- Al elegir recinto, cargamos sus zonas reales ---
  useEffect(() => {
    if (esEdicion || !form.recintoId) {
      setZonasDelRecinto([]);
      return;
    }
    (async () => {
      try {
        const zonasDisponibles = await getZonasDeRecinto(form.recintoId);
        setZonasDelRecinto(zonasDisponibles);
      } catch (error) {
        console.error("Error al cargar las zonas del recinto:", error);
      }
    })();
  }, [esEdicion, form.recintoId]);

  // --- Modo edición: traemos el evento real ---
  useEffect(() => {
    if (!esEdicion) {
      setCargando(false);
      return;
    }

    async function cargarEvento() {
      setCargando(true);
      try {
        const data = await getEventoParaEditar(eventoId);
        setEventoExistente(data);
        setEstadoSeleccionado(data.estado || "PROGRAMADO");
      } catch (error) {
        console.error("Error al cargar el evento:", error);
      } finally {
        setCargando(false);
      }
    }
    cargarEvento();
  }, [esEdicion, eventoId]);

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    setSubiendoImagen(true);
    try {
      const uploadResponse = await subirImagen(file);
      setFlyerUrl(uploadResponse.url);
    } catch (error) {
      console.error("Error al subir la imagen:", error);
      alert("Hubo un problema al subir la imagen");
    } finally {
      setSubiendoImagen(false);
    }
  };

  const handleChangeNuevaZona = (e) => {
    setNuevaZona({ ...nuevaZona, [e.target.name]: e.target.value });
  };

  const handleAgregarZona = () => {
    if (!nuevaZona.zonaId || !nuevaZona.precio || !nuevaZona.asientos) return;

    const zonaInfo = zonasDelRecinto.find((z) => String(z.id) === String(nuevaZona.zonaId));

    setZonas((prev) => [
      ...prev,
      {
        idTemporal: Date.now(),
        zonaId: nuevaZona.zonaId,
        nombreZona: zonaInfo ? zonaInfo.nombre : nuevaZona.zonaId,
        precio: nuevaZona.precio,
        asientos: nuevaZona.asientos,
      },
    ]);

    setNuevaZona({ zonaId: "", precio: "", asientos: "" });
  };

  const handleEliminarZona = (idTemporal) => {
    setZonas((prev) => prev.filter((z) => z.idTemporal !== idTemporal));
  };

  const handleCrear = async () => {
    if (!validar()) return;
    if (zonas.length === 0) {
      alert("Agrega al menos una zona para el evento.");
      return;
    }

    setGuardando(true);
    try {
      await crearEvento({
        nombre: form.nombreEvento,
        descripcion: form.descripcion,
        flyerPrincipal: flyerUrl,
        fechas: [
          {
            fecha: form.fecha,
            hora: form.hora,
            ciudad: form.ciudad,
            recintoId: Number(form.recintoId),
          },
        ],
        boletos: zonas.map((z) => ({
          zonaId: Number(z.zonaId),
          precio: Number(z.precio),
          cantidadDisponible: Number(z.asientos),
        })),
      });
      navigate("/organizador/dashboard");
    } catch (error) {
      console.error("Error al crear el evento:", error);
      const mensaje = error.response?.data?.message || "No se pudo crear el evento.";
      alert(mensaje);
    } finally {
      setGuardando(false);
    }
  };

  const handleGuardarEstado = async () => {
    setGuardando(true);
    try {
      await cambiarEstadoEvento(eventoId, estadoSeleccionado);
      navigate("/organizador/dashboard");
    } catch (error) {
      console.error("Error al cambiar el estado del evento:", error);
      const mensaje = error.response?.data?.message || "No se pudo actualizar el estado.";
      alert(mensaje);
    } finally {
      setGuardando(false);
    }
  };

  if (cargando) {
    return <p className="evento-form-loading">Cargando evento...</p>;
  }

  return (
    <div className="evento-form-page">
      <div className="evento-form-topbar">
        <span className="evento-form-logo" onClick={() => navigate("/organizador/dashboard")}>
          <img src={logo} alt="Tessera" className="evento-form-logo-img" />
          Tessera
        </span>
        <button className="evento-form-usuario-btn">
          <i className="ti ti-user"></i>
          Usuario
        </button>
      </div>

      {esEdicion ? (
        // --- Modo edición: el backend todavía no expone un endpoint para editar
        // nombre/fechas/zonas de un evento ya creado, solo su estado. ---
        <div className="evento-form-card">
          <p style={{ marginBottom: 16 }}>
            <strong>{eventoExistente?.nombre}</strong>
          </p>
          <p style={{ marginBottom: 16, color: "var(--color-text-muted)" }}>
            {eventoExistente?.descripcion}
          </p>
          <p style={{ marginBottom: 20, fontSize: 13, color: "var(--color-text-muted)" }}>
            La edición completa de este evento (nombre, fechas, zonas) todavía no está
            disponible: el backend solo permite cambiar su estado por ahora.
          </p>

          <div className="input-icon" style={{ marginBottom: 20 }}>
            <i className="ti ti-flag"></i>
            <select
              value={estadoSeleccionado}
              onChange={(e) => setEstadoSeleccionado(e.target.value)}
            >
              {ESTADOS.map((estado) => (
                <option key={estado} value={estado}>
                  {estado}
                </option>
              ))}
            </select>
          </div>

          <button
            className="evento-form-registrar-btn"
            onClick={handleGuardarEstado}
            disabled={guardando}
          >
            {guardando ? "Guardando..." : "Guardar estado"}
          </button>
        </div>
      ) : (
        <div className="evento-form-card">
          <div className="evento-form-columns">
            {/* Columna 1: datos del evento */}
            <div className="evento-form-col">
              <FormField
                icon="ti-tag"
                type="text"
                name="nombreEvento"
                placeholder="Nombre del evento"
                value={form.nombreEvento}
                onChange={handleChange}
                error={errores.nombreEvento}
              />
              <FormField
                icon="ti-align-left"
                type="text"
                name="descripcion"
                placeholder="Descripción"
                value={form.descripcion}
                onChange={handleChange}
                error={errores.descripcion}
              />
              <FormField
                icon="ti-map-pin"
                type="text"
                name="ciudad"
                placeholder="Ciudad"
                value={form.ciudad}
                onChange={handleChange}
                error={errores.ciudad}
              />

              <div className={`input-icon ${errores.recintoId ? "has-error" : ""}`}>
                <i className="ti ti-building"></i>
                <select name="recintoId" value={form.recintoId} onChange={handleChange}>
                  <option value="">Selecciona un recinto</option>
                  {misRecintos.map((r) => (
                    <option key={r.id} value={r.id}>
                      {r.nombre}
                    </option>
                  ))}
                </select>
              </div>
              {errores.recintoId && <span className="input-error">{errores.recintoId}</span>}
              {misRecintos.length === 0 && (
                <p style={{ fontSize: 12, color: "var(--color-text-muted)" }}>
                  Todavía no tienes recintos registrados. Crea uno primero para poder
                  seleccionarlo aquí.
                </p>
              )}

              <FormField
                icon="ti-calendar"
                type="date"
                name="fecha"
                value={form.fecha}
                onChange={handleChange}
                error={errores.fecha}
              />
              <FormField
                icon="ti-clock"
                type="time"
                name="hora"
                value={form.hora}
                onChange={handleChange}
                error={errores.hora}
              />
            </div>

            {/* Columna 2: imágenes + formulario para agregar una zona nueva */}
            <div className="evento-form-col">
              <label className="evento-form-imagenes" style={{ cursor: "pointer" }}>
                <i className="ti ti-photo evento-form-imagenes-icon"></i>
                <span>{subiendoImagen ? "Subiendo..." : flyerUrl ? "Imagen lista ✓" : "Imágenes"}</span>
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                  style={{ display: "none" }}
                />
              </label>

              <div className="input-icon">
                <i className="ti ti-map-2"></i>
                <select
                  name="zonaId"
                  value={nuevaZona.zonaId}
                  onChange={handleChangeNuevaZona}
                  disabled={!form.recintoId}
                >
                  <option value="">
                    {form.recintoId ? "Selecciona una zona" : "Elige primero un recinto"}
                  </option>
                  {zonasDelRecinto.map((z) => (
                    <option key={z.id} value={z.id}>
                      {z.nombre}
                    </option>
                  ))}
                </select>
              </div>

              <div className="input-icon">
                <i className="ti ti-currency-dollar"></i>
                <input
                  type="number"
                  name="precio"
                  placeholder="Precio por zona"
                  value={nuevaZona.precio}
                  onChange={handleChangeNuevaZona}
                />
              </div>

              <div className="input-icon">
                <i className="ti ti-armchair"></i>
                <input
                  type="number"
                  name="asientos"
                  placeholder="Asientos por zona"
                  value={nuevaZona.asientos}
                  onChange={handleChangeNuevaZona}
                />
              </div>

              <button type="button" className="evento-form-agregar-zona" onClick={handleAgregarZona}>
                <i className="ti ti-plus"></i>
                Añadir zona
              </button>
            </div>

            {/* Columna 3: lista de zonas ya agregadas */}
            <div className="evento-form-col evento-form-zonas-col">
              {zonas.length === 0 ? (
                <p className="evento-form-zonas-vacio">Aún no agregas zonas.</p>
              ) : (
                zonas.map((z) => (
                  <div key={z.idTemporal} className="zona-card">
                    <button
                      className="zona-eliminar"
                      onClick={() => handleEliminarZona(z.idTemporal)}
                      aria-label="Eliminar zona"
                    >
                      <i className="ti ti-x"></i>
                    </button>
                    <div className="zona-info">
                      <p>
                        Zona: <strong>{z.nombreZona}</strong>
                      </p>
                      <p>
                        Precio: <strong>${Number(z.precio).toLocaleString("es-MX")}</strong>
                      </p>
                      <p>
                        Asientos: <strong>{z.asientos}</strong>
                      </p>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>

          <button className="evento-form-registrar-btn" onClick={handleCrear} disabled={guardando}>
            {guardando ? "Guardando..." : "Registrarme"}
          </button>
        </div>
      )}
    </div>
  );
}
