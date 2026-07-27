export default function EventCard({ evento }) {
  return (
    <div className="event-card">
      <div className="event-card-image">
        {evento.imagenUrl ? (
          <img src={evento.imagenUrl} alt={evento.nombreEvento} />
        ) : (
          <span className="event-card-placeholder">{evento.nombreEvento}</span>
        )}
      </div>
      <p className="event-card-title">{evento.nombreEvento}</p>
      <p className="event-card-subtitle">{evento.nombreRecinto}</p>
    </div>
  );
}