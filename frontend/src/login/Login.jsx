import { useNavigate, Link } from "react-router-dom";
import logo from "../assets/img/logo.png";
import heroImg from "../assets/img/recinto.png";
import { useValidacion } from "../hooks/useValidacion";
import { requerido, esEmail } from "../hooks/validadores";
import FormField from "../components/FormField";
import "./Login.css";

export default function Login() {
  const { form, errores, handleChange, validar } = useValidacion(
    { email: "", password: "" },
    {
      email: [requerido, esEmail],
      password: [requerido],
    }
  );

  const navigate = useNavigate();

  const handleLogin = (rol) => {
    if (!validar()) return;

    // aquí luego conectamos con api.js para el login real con JWT
    console.log("Login como", rol, form);

    if (rol === "empresa") navigate("/empresa/dashboard");
    else navigate("/");
  };

  return (
    <div className="login-page">
      <div className="login-logo">
        <img src={logo} alt="Tessera" className="login-logo-img" />
        <span>Tessera</span>
      </div>

      <div className="login-content">
        <div className="login-form-side">
          <h1 className="login-title">Iniciar Sesión</h1>

          <div className="login-card">
            <FormField
              icon="ti-at"
              type="email"
              name="email"
              placeholder="Correo"
              value={form.email}
              onChange={handleChange}
              error={errores.email}
            />
            <FormField
              icon="ti-lock"
              type="password"
              name="password"
              placeholder="Contraseña"
              value={form.password}
              onChange={handleChange}
              error={errores.password}
            />

            <label className="login-remember">
              <input type="checkbox" />
              Recordarme
            </label>

            <div className="login-buttons">
              <button className="btn-navy" onClick={() => handleLogin("cliente")}>
                Cliente
              </button>
              <button className="btn-navy" onClick={() => handleLogin("empresa")}>
                Empresa
              </button>
            </div>

            <p className="login-register-text">
              ¿No tienes cuenta? Regístrate aquí <br />
              <Link to="/registro/cliente">Cliente</Link> /{" "}
              <Link to="/registro/empresa">Empresa</Link>
            </p>

            <div className="login-divider">
              <span>O continuar con</span>
            </div>

            <div className="login-social">
              <i className="ti ti-brand-google"></i>
              <i className="ti ti-brand-facebook"></i>
              <i className="ti ti-brand-apple"></i>
            </div>
          </div>
        </div>

        <div className="login-image-side">
          <img src={heroImg} alt="Recinto" />
        </div>
      </div>
    </div>
  );
}