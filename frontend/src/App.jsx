import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
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
import DashboardAdmin from './administrador/DashboardAdmin';
import GestionRecintos from './administrador/GestionRecintos';

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
          <Route path="/evento/:id/asientos" element={<SeleccionAsientos />} />
          <Route path="/confirmacion" element={<Confirmacion />} />
          <Route path="/mis-boletos" element={<MisBoletos />} />
          <Route path="/perfil" element={<Perfil />} />

          {/* pag empresa */}
          <Route path="/empresa/dashboard" element={<DashboardEmpresa />} />
          <Route path="/empresa/evento/nuevo" element={<CrearEditarEvento />} />

          {/* pag administrador */}
          <Route path="/admin/dashboard" element={<DashboardAdmin />} />
          <Route path="/admin/recintos" element={<GestionRecintos />} />
        </Routes>
      </div>
    </>
  );
}

export default App;