import { Routes, Route } from 'react-router-dom';
import Login from './login/Login';
import RegistroCliente from './registro/RegistroCliente';
import RegistroEmpresa from './registro/RegistroEmpresa';
import Home from './cliente/Home';
import Carrito from './cliente/Carrito';
import SeleccionAsientos from './cliente/SeleccionAsientos';
import Confirmacion from './cliente/Confirmacion';
import MisBoletos from './cliente/MisBoletos';
import Perfil from './cliente/Perfil';
import DashboardEmpresa from './empresa/Dashboard';
import CrearEditarEvento from './empresa/CrearEditarEvento';
import GestionRecintos from './administrador/GestionRecintos';
import DashboardAdmin from './administrador/DashboardAdmin';

function App() {
  return (
    <>
      <div className="main-content">
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
      </div>
    </>
  );
}

export default App;