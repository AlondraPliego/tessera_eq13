import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import logo from "../assets/img/logo.png";
import heroImg from "../assets/img/recinto.png";
import { useValidacion } from "../hooks/useValidacion";
import { requerido, esEmail } from "../hooks/validadores";
import FormField from "../components/FormField";
import api from "../services/api";
import { useAuth } from "../AuthContext";
import "./Login.css";

export default function Login() {
  const { form, errores, handleChange, validar } = useValidacion(
    { email: "", password: "" },
    {
      email: [requerido, esEmail],
      password: [requerido],
    }
  );

  const [errorApi, setErrorApi] = useState("");
  const [enviando, setEnviando] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validar()) return;

    setErrorApi("");
    setEnviando(true);
    try {
      const { data } = await api.post("/api/auth/login", {
        email: form.email,
        password: form.password,
      });

      login({ email: data.email, rol: data.rol }, data.token);

      if (data.rol === "EMPRESA") navigate("/empresa/dashboard");
      else navigate("/");
    } catch (err) {
      setErrorApi(
        err.response?.data?.message ||
          err.response?.data ||
          "Correo o contraseña incorrectos."
      );
    } finally {
      setEnviando(false);
    }
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

          <form onSubmit={handleSubmit} noValidate className="login-card">
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

            {errorApi && <p className="input-error">{errorApi}</p>}

            <div className="login-buttons">
              <button type="submit" className="btn-navy" disabled={enviando}>
                {enviando ? "Ingresando..." : "Iniciar sesión"}
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
          </form>
        </div>

        <div className="login-image-side">
          <img src={heroImg} alt="Recinto" />
        </div>
      </div>
    </div>
  );
}