import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getRecintos, crearRecinto, vincularSchemaSeatmap } from "../services/venueService";
import "./GestionRecintos.css";

const SEATMAP_EDITOR_URL = "https://editor.seatmap.pro"; // URL real del Editor de tu cuenta Seatmap Pro

export default function GestionRecintos() {
  const navigate = useNavigate();
  const [recintos, setRecintos] = useState([]);
  const [cargando, setCargando] = useState(true);

  const [nuevoRecinto, setNuevoRecinto] = useState({ nombre: "", direccion: "" });
  const [vinculando, setVinculando] = useState(null); // id del recinto que se está vinculando
  const [schemaForm, setSchemaForm] = useState({ schemaId: "", publicKey: "" });

  useEffect(() => {
    async function cargar() {
      setCargando(true);
      try {
        const data = await getRecintos();
        setRecintos(data);
      } catch (error) {
        console.error("Error al cargar recintos:", error);
      } finally {
        setCargando(false);
      }
    }
    cargar();
  }, []);

  const handleCrearRecinto = async () => {
    if (!nuevoRecinto.nombre || !nuevoRecinto.direccion) return;
    try {
      const creado = await crearRecinto(nuevoRecinto);
      setRecintos((prev) => [...prev, creado]);
      setNuevoRecinto({ nombre: "", direccion: "" });
    } catch (error) {
      console.error("Error al crear recinto:", error);
    }
  };

  const handleAbrirVincular = (recintoId) => {
    setVinculando(recintoId);
    setSchemaForm({ schemaId: "", publicKey: "" });
  };

  const handleGuardarSchema = async (recintoId) => {
    if (!schemaForm.schemaId || !schemaForm.publicKey) return;
    try {
      await vincularSchemaSeatmap(recintoId, schemaForm.schemaId, schemaForm.publicKey);
      setRecintos((prev) =>
        prev.map((r) =>
          r.id === recintoId
            ? { ...r, schemaId: schemaForm.schemaId, publicKey: schemaForm.publicKey, estado: "Publicado" }
            : r
        )
      );
      setVinculando(null);
    } catch (error) {
      console.error("Error al vincular el schema:", error);
    }
  };

  if (cargando) {
    return <p className="recintos-loading">Cargando recintos...</p>;
  }

  return (
    <div className="recintos-page">
      <div className="recintos-topbar">
        <span className="recintos-logo" onClick={() => navigate("/admin/dashboard")}>
          Tessera
        </span>
        <button className="recintos-volver-btn" onClick={() => navigate("/admin/dashboard")}>
          Volver al dashboard
        </button>
      </div>

      <div className="recintos-header">
        <h1 className="recintos-title">Gestión de recintos</h1>
        <p className="recintos-subtitle">
          Registra tus recintos y vincula su mapa base diseñado en el Editor de Seatmap Pro.
        </p>
      </div>

      <div className="recintos-nuevo-card">
        <h2 className="recintos-card-title">Registrar nuevo recinto</h2>
        <div className="recintos-nuevo-form">
          <input
            type="text"
            placeholder="Nombre del recinto"
            value={nuevoRecinto.nombre}
            onChange={(e) => setNuevoRecinto({ ...nuevoRecinto, nombre: e.target.value })}
          />
          <input
            type="text"
            placeholder="Dirección"
            value={nuevoRecinto.direccion}
            onChange={(e) => setNuevoRecinto({ ...nuevoRecinto, direccion: e.target.value })}
          />
          <button className="recintos-crear-btn" onClick={handleCrearRecinto}>
            Registrar
          </button>
        </div>
      </div>

      <div className="recintos-list">
        {recintos.map((recinto) => (
          <div key={recinto.id} className="recinto-card">
            <div className="recinto-info">
              <p className="recinto-nombre">{recinto.nombre}</p>
              <p className="recinto-direccion">{recinto.direccion}</p>
              {recinto.schemaId && (
                <p className="recinto-schema-id">Schema ID: {recinto.schemaId}</p>
              )}
            </div>

            <span
              className={`recinto-estado ${
                recinto.estado === "Publicado" ? "publicado" : "sin-mapa"
              }`}
            >
              {recinto.estado}
            </span>

            <div className="recinto-actions">
              
                href={SEATMAP_EDITOR_URL}
                target="_blank"
                rel="noopener noreferrer"
                className="recinto-btn-editor"
            <a>
                Abrir Editor de mapa
              </a>
              <button
                className="recinto-btn-vincular"
                onClick={() => handleAbrirVincular(recinto.id)}
              >
                {recinto.schemaId ? "Actualizar schema" : "Vincular schema"}
              </button>
            </div>

            {vinculando === recinto.id && (
              <div className="recinto-vincular-form">
                <p className="recinto-vincular-help">
                  Copia el <strong>Schema ID</strong> y el <strong>Public API Key</strong> desde
                  el panel de administración de tu organización en Seatmap Pro.
                </p>
                <input
                  type="text"
                  placeholder="Schema ID (ej. 3275)"
                  value={schemaForm.schemaId}
                  onChange={(e) => setSchemaForm({ ...schemaForm, schemaId: e.target.value })}
                />
                <input
                  type="text"
                  placeholder="Public API Key (ej. pk_...)"
                  value={schemaForm.publicKey}
                  onChange={(e) => setSchemaForm({ ...schemaForm, publicKey: e.target.value })}
                />
                <div className="recinto-vincular-buttons">
                  <button
                    className="recinto-btn-cancelar"
                    onClick={() => setVinculando(null)}
                  >
                    Cancelar
                  </button>
                  <button
                    className="recinto-btn-guardar"
                    onClick={() => handleGuardarSchema(recinto.id)}
                  >
                    Guardar
                  </button>
                </div>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}