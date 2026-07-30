import { Suspense, lazy } from 'react';
import { Routes, Route } from 'react-router-dom';

// Home se queda con import estático porque es la primera pantalla que ve todo mundo
import Home from './cliente/Home';

// El resto se carga solo cuando el usuario navega a esa ruta.
const Login = lazy(() => import('./login/Login'));
const RegistroCliente = lazy(() => import('./registro/RegistroCliente'));
const RegistroEmpresa = lazy(() => import('./registro/RegistroEmpresa'));
const Carrito = lazy(() => import('./cliente/Carrito'));
const SeleccionAsientos = lazy(() => import('./cliente/SeleccionAsientos'));
const Confirmacion = lazy(() => import('./cliente/Confirmacion'));
const MisBoletos = lazy(() => import('./cliente/MisBoletos'));
const Perfil = lazy(() => import('./cliente/Perfil'));
const DashboardEmpresa = lazy(() => import('./empresa/Dashboard'));
const CrearEditarEvento = lazy(() => import('./empresa/CrearEditarEvento'));
const GestionRecintos = lazy(() => import('./administrador/GestionRecintos'));
const DashboardAdmin = lazy(() => import('./administrador/DashboardAdmin'));

function App() {
  return (
    <>
      <div className="main-content">
        <Suspense fallback={<p style={{ padding: 24 }}>Cargando...</p>}>
        <Routes>
          {/* pag publicas */}
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/registro/cliente" element={<RegistroCliente />} />
          <Route path="/registro/empresa" element={<RegistroEmpresa />} />

          {/* pag cliente */}
          <Route path="/carrito" element={<Carrito />} />
          <Route path="/evento/:eventoId/asientos" element={<SeleccionAsientos />} />
          {/* Carrito.jsx navega a /confirmacion/:folio, hacia falta el parametro */}
          <Route path="/confirmacion/:folio" element={<Confirmacion />} />
          <Route path="/mis-boletos" element={<MisBoletos />} />
          <Route path="/perfil" element={<Perfil />} />

          {/* pag empresa (el backend solo permite ROLE_EMPRESA crear/editar recintos y eventos) */}
          <Route path="/empresa/dashboard" element={<DashboardEmpresa />} />
          <Route path="/empresa/evento/nuevo" element={<CrearEditarEvento />} />
          <Route path="/empresa/evento/:eventoId/editar" element={<CrearEditarEvento />} />
          <Route path="/empresa/recintos" element={<GestionRecintos />} />

          {/* pag administrador */}
          <Route path="/admin/dashboard" element={<DashboardAdmin />} />
        </Routes>
        </Suspense>
      </div>
    </>
  );
}

export default App;