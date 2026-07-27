import { Routes, Route } from "react-router-dom";
import Login from "./login/Login";
import RegistroCliente from "./registro/RegistroCliente";
import RegistroEmpresa from "./registro/RegistroEmpresa";
import Home from "./cliente/Home";
import SeleccionAsientos from "./cliente/SeleccionAsientos";
import Carrito from "./cliente/Carrito";
import Confirmacion from "./cliente/Confirmacion";
import MisBoletos from "./cliente/MisBoletos";
import Perfil from "./cliente/Perfil";
import Dashboard from "./empresa/Dashboard";
import CrearEditarEvento from "./empresa/CrearEditarEvento";
import DashboardAdmin from "./administrador/DashboardAdmin";
import GestionRecintos from "./administrador/GestionRecintos";
function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/registro/cliente" element={<RegistroCliente />} />
      <Route path="/registro/empresa" element={<RegistroEmpresa />} />
      <Route path="/evento/:eventoId/asientos" element={<SeleccionAsientos />} />

<Route path="/carrito" element={<Carrito />} />
<Route path="/confirmacion/:folio" element={<Confirmacion />} />
<Route path="/mis-boletos" element={<MisBoletos />} />
<Route path="/perfil" element={<Perfil />} />
<Route path="/organizador/dashboard" element={<Dashboard />} />
<Route path="/organizador/eventos/nuevo" element={<CrearEditarEvento />} />
<Route path="/organizador/eventos/:eventoId/editar" element={<CrearEditarEvento />} />
<Route path="/admin/dashboard" element={<DashboardAdmin />} />
<Route path="/admin/recintos" element={<GestionRecintos />} />
    </Routes>
  );
}

export default App;