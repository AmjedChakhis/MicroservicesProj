// src/pages/Dashboard.js
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'; // for navigation
import { PieChart, Pie, Cell, Legend, Tooltip } from 'recharts'; // for visualizations
import Header from '../components/Header'; 
import './Dashboard.css';

function Dashboard() {
    const [clientsCount, setClientsCount] = useState(0);
    const [vehiclesCount, setVehiclesCount] = useState(0);
    const [maintenanceCount, setMaintenanceCount] = useState(0);
    const [vehicleStatusData, setVehicleStatusData] = useState([
        { name: 'Functional', value: 0 },
        { name: 'In Repair', value: 0 },
        { name: 'Pending', value: 0 },
    ]);

    useEffect(() => {
        // Simulating API calls for statistics (replace with actual API calls)
        setClientsCount(50); // e.g., get from API
        setVehiclesCount(120); // e.g., get from API
        setMaintenanceCount(30); // e.g., get from API

        // Simulating vehicle status data
        setVehicleStatusData([
            { name: 'Functional', value: 80 },
            { name: 'In Repair', value: 30 },
            { name: 'Pending', value: 10 },
        ]);
    }, []);

    return (
        <div>
            <Header />
            <h1>Dashboard</h1>
            <p className="welcome-text">Welcome to the Garage Management System!</p>


            {/* Key Stats Section */}
            <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '30px' }}>
    <div style={{ textAlign: 'center', padding: '20px', margin: '0 10px', border: '1px solid #ddd', borderRadius: '8px', flex: '1' }}>
        <h2>{clientsCount}</h2>
        <p>Clients</p>
    </div>
    <div style={{
        textAlign: 'center',
        padding: '20px',
        margin: '0 10px',
        border: '1px solid #ddd',
        borderRadius: '8px',
        flex: '0 0 auto', // Ensures the Vehicles card does not stretch
        width: '200px', // Fixes card width for centering
    }}>
        <h2>{vehiclesCount}</h2>
        <p>Vehicles</p>
    </div>
    <div style={{ textAlign: 'center', padding: '20px', margin: '0 10px', border: '1px solid #ddd', borderRadius: '8px', flex: '1' }}>
        <h2>{maintenanceCount}</h2>
        <p>Planned Maintenance</p>
    </div>
</div>


            {/* Vehicle Status Pie Chart */}
            <h3>Vehicle Status</h3>
            <div className="pie-chart-container">
    <PieChart width={400} height={300}>
        <Pie
            data={vehicleStatusData}
            cx="50%" /* Center horizontally */
            cy="50%" /* Center vertically */
            outerRadius={100}
            fill="#8884d8"
            dataKey="value"
        >
            {vehicleStatusData.map((entry, index) => (
                <Cell
                    key={`cell-${index}`}
                    fill={index === 0 ? '#4CAF50' : index === 1 ? '#FF9800' : '#FF5722'}
                />
            ))}
        </Pie>
        <Legend />
        <Tooltip />
    </PieChart>
</div>



            {/* Quick Action Links */}
            <div style={{ marginTop: '30px' }}>
                <h3>Quick Actions</h3>
                <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                    <Link to="/clients" style={buttonStyle}>Manage Clients</Link>
                    <Link to="/vehicles" style={buttonStyle}>Manage Vehicles</Link>
                    <Link to="/planning" style={buttonStyle}>Plan Maintenance</Link>
                </div>
            </div>
        </div>
    );
}

// Simple style for the quick action buttons
const buttonStyle = {
    padding: '10px 20px',
    backgroundColor: '#4CAF50',
    color: '#fff',
    textDecoration: 'none',
    borderRadius: '5px',
    textAlign: 'center',
};

export default Dashboard;
