// App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import Clients from './pages/Clients.jsx';
import Vehicles from './pages/Vehicles';
import Planning from './pages/Planning';

function App() {
  return (
    <Router>
      <div>

        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/clients" element={<Clients />} />
          <Route path="/vehicles" element={<Vehicles />} />
          <Route path="/planning" element={<Planning />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
