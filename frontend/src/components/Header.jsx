// src/components/Header.js
import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

function Header() {
    return (
        <div className="header">
            <Link to="/" className="logo">
                <img 
                    src={require('../assets/garage.png')} // Adjust the path based on your project structure
                    alt="Garage Management Logo"
                    className="logo-img"
                />
            </Link>
            <nav className="nav-links">
                <Link to="/" className="nav-link">Dashboard</Link>
                <Link to="/clients" className="nav-link">Clients</Link>
                <Link to="/vehicles" className="nav-link">Vehicles</Link>
                <Link to="/planning" className="nav-link">Planning</Link>
            </nav>
        </div>
    );
}

export default Header;
