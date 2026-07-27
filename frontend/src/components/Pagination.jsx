export default function Pagination({ paginaActual, totalPaginas, onCambiarPagina }) {
  const paginas = Array.from({ length: totalPaginas }, (_, i) => i + 1);

  return (
    <div className="pagination">
      <button
        className="pagination-arrow"
        onClick={() => onCambiarPagina(paginaActual - 1)}
        disabled={paginaActual === 1}
      >
        <i className="ti ti-chevron-left"></i>
      </button>

      {paginas.map((numero) => (
        <button
          key={numero}
          className={`pagination-number ${numero === paginaActual ? "active" : ""}`}
          onClick={() => onCambiarPagina(numero)}
        >
          {numero}
        </button>
      ))}

      <button
        className="pagination-arrow"
        onClick={() => onCambiarPagina(paginaActual + 1)}
        disabled={paginaActual === totalPaginas}
      >
        <i className="ti ti-chevron-right"></i>
      </button>
    </div>
  );
}