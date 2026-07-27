import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import logo from "../assets/img/logo.png";
import { useValidacion } from "../hooks/useValidacion";
import { requerido } from "../hooks/validadores";
import FormField from "../components/FormField";
import { getEventoParaEditar, guardarEvento } from "../services/organizerService";
import "./CrearEditarEvento.css";

export default function CrearEditarEvento() {
  const { eventoId } = useParams();
  const esEdicion = Boolean(eventoId);
  const navigate = useNavigate();
  const [cargando, setCargando] = useState(esEdicion);

  const { form, errores, handleChange, validar, setForm } = useValidacion(
    {
      identificador: "",
      nombreEvento: "",
      numeroEvento: "",
      recinto: "",
      fechaHora: "",
      descripcion: "",
    },
    {
      nombreEvento: [requerido],
      recinto: [requerido],
      fechaHora: [requerido],
    }
  );

  const [nuevaZona, setNuevaZona] = useState({
    zona: "",
    precio: "",
    asientos: "",
  });

  // --- Lista de zonas ya agregadas ---
  const [zonas, setZonas] = useState([]);

  useEffect(() => {
    if (!esEdicion) return;

    async function cargarEvento() {
      setCargando(true);
      try {
        const data = await getEventoParaEditar(eventoId);
        setForm({
          identificador: data.identificador || "",
          nombreEvento: data.nombreEvento || "",
          numeroEvento: data.numeroEvento || "",
          recinto: data.recinto || "",
          fechaHora: data.fechaHora || "",
          descripcion: data.descripcion || "",
        });
        setZonas(data.zonas || []);
      } catch (error) {
        console.error("Error al cargar el evento:", error);
      } finally {
        setCargando(false);
      }
    }
    cargarEvento();
  }, [eventoId]); // eslint-disable-line react-hooks/exhaustive-deps

  const handleChangeNuevaZona = (e) => {
    setNuevaZona({ ...nuevaZona, [e.target.name]: e.target.value });
  };

  const handleAgregarZona = () => {
    if (!nuevaZona.zona || !nuevaZona.precio || !nuevaZona.asientos) return;

    setZonas((prev) => [
      ...prev,
      {
        id: Date.now(),
        zona: nuevaZona.zona,
        precio: nuevaZona.precio,
        asientos: nuevaZona.asientos,
      },
    ]);

    setNuevaZona({ zona: "", precio: "", asientos: "" });
  };

  const handleEliminarZona = (id) => {
    setZonas((prev) => prev.filter((z) => z.id !== id));
  };

  const handleGuardar = async () => {
    if (!validar()) return;
    if (zonas.length === 0) {
      alert("Agrega al menos una zona para el evento.");
      return;
    }

    try {
      await guardarEvento({ ...form, zonas }, eventoId);
      navigate("/organizador/dashboard");
    } catch (error) {
      console.error("Error al guardar el evento:", error);
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

      <div className="evento-form-card">
        <div className="evento-form-columns">
          {/* Columna 1: datos del evento */}
          <div className="evento-form-col">
            <FormField
              icon="ti-id"
              type="text"
              name="identificador"
              placeholder="Identificador"
              value={form.identificador}
              onChange={handleChange}
              error={errores.identificador}
            />
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
              icon="ti-number"
              type="text"
              name="numeroEvento"
              placeholder="Número de evento"
              value={form.numeroEvento}
              onChange={handleChange}
              error={errores.numeroEvento}
            />
            <FormField
              icon="ti-map-pin"
              type="text"
              name="recinto"
              placeholder="Recinto"
              value={form.recinto}
              onChange={handleChange}
              error={errores.recinto}
            />
            <FormField
              icon="ti-calendar"
              type="datetime-local"
              name="fechaHora"
              value={form.fechaHora}
              onChange={handleChange}
              error={errores.fechaHora}
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
          </div>

          {/* Columna 2: imágenes + formulario para agregar una zona nueva */}
          <div className="evento-form-col">
            <div className="evento-form-imagenes">
              <i className="ti ti-photo evento-form-imagenes-icon"></i>
              <span>Imágenes</span>
            </div>

            <div className="input-icon">
              <i className="ti ti-map-2"></i>
              <input
                type="text"
                name="zona"
                placeholder="Zona"
                value={nuevaZona.zona}
                onChange={handleChangeNuevaZona}
              />
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
                <div key={z.id} className="zona-card">
                  <button
                    className="zona-eliminar"
                    onClick={() => handleEliminarZona(z.id)}
                    aria-label="Eliminar zona"
                  >
                    <i className="ti ti-x"></i>
                  </button>
                  <div className="zona-info">
                    <p>
                      Zona: <strong>{z.zona}</strong>
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

        <button className="evento-form-registrar-btn" onClick={handleGuardar}>
          {esEdicion ? "Guardar cambios" : "Registrarme"}
        </button>
      </div>
    </div>
  );
}