export default function FormField({ icon, error, label, ...inputProps }) {
  return (
    <div className="form-field">
      {label && <label className="form-field-label">{label}</label>}
      <div className={`input-icon ${error ? "has-error" : ""}`}>
        <i className={`ti ${icon}`}></i>
        <input {...inputProps} />
      </div>
      {error && <span className="input-error">{error}</span>}
    </div>
  );
}