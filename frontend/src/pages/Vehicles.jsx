import React, { useState, useEffect } from 'react';
import Header from '../components/Header';
import './Vehicles.css';

function Vehicles() {
    const [vehicles, setVehicles] = useState([]);
    const [newVehicle, setNewVehicle] = useState({
        vin: '',
        registration: '',
        brand: '',
        model: '',
        year: '',
        color: '',
        mileage: '',
        fuelType: '',
        purchaseDate: '',
        owner: '',
        status: ''
    });

    useEffect(() => {
        // Fetch all vehicles from the backend
        fetch('/api/vehicles')
            .then((response) => response.json())
            .then((data) => setVehicles(data))
            .catch((error) => console.error('Error fetching vehicles:', error));
    }, []);

    const handleAddVehicle = (e) => {
        e.preventDefault();
        fetch('/api/vehicles', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newVehicle)
        })
            .then((response) => response.json())
            .then((createdVehicle) => {
                setVehicles([...vehicles, createdVehicle]);
                setNewVehicle({
                    vin: '',
                    registration: '',
                    brand: '',
                    model: '',
                    year: '',
                    color: '',
                    mileage: '',
                    fuelType: '',
                    purchaseDate: '',
                    owner: '',
                    status: ''
                });
            })
            .catch((error) => console.error('Error adding vehicle:', error));
    };

    return (
        <div>
            <Header />
            <h1>Vehicles</h1>
            <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th>VIN</th>
                        <th>Registration</th>
                        <th>Brand</th>
                        <th>Model</th>
                        <th>Year</th>
                        <th>Color</th>
                        <th>Mileage</th>
                        <th>Fuel Type</th>
                        <th>Purchase Date</th>
                        <th>Owner</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    {vehicles.map((vehicle) => (
                        <tr key={vehicle.vin}>
                            <td>{vehicle.vin}</td>
                            <td>{vehicle.registration}</td>
                            <td>{vehicle.brand}</td>
                            <td>{vehicle.model}</td>
                            <td>{vehicle.year}</td>
                            <td>{vehicle.color}</td>
                            <td>{vehicle.mileage}</td>
                            <td>{vehicle.fuelType}</td>
                            <td>{vehicle.purchaseDate}</td>
                            <td>{vehicle.owner}</td>
                            <td>{vehicle.status}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <h2>Add New Vehicle</h2>
            <form onSubmit={handleAddVehicle}>
                <input
                    type="text"
                    placeholder="VIN"
                    value={newVehicle.vin}
                    onChange={(e) => setNewVehicle({ ...newVehicle, vin: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Registration"
                    value={newVehicle.registration}
                    onChange={(e) => setNewVehicle({ ...newVehicle, registration: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Brand"
                    value={newVehicle.brand}
                    onChange={(e) => setNewVehicle({ ...newVehicle, brand: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Model"
                    value={newVehicle.model}
                    onChange={(e) => setNewVehicle({ ...newVehicle, model: e.target.value })}
                    required
                />
                <input
                    type="number"
                    placeholder="Year"
                    value={newVehicle.year}
                    onChange={(e) => setNewVehicle({ ...newVehicle, year: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Color"
                    value={newVehicle.color}
                    onChange={(e) => setNewVehicle({ ...newVehicle, color: e.target.value })}
                    required
                />
                <input
                    type="number"
                    placeholder="Mileage"
                    value={newVehicle.mileage}
                    onChange={(e) => setNewVehicle({ ...newVehicle, mileage: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Fuel Type"
                    value={newVehicle.fuelType}
                    onChange={(e) => setNewVehicle({ ...newVehicle, fuelType: e.target.value })}
                    required
                />
                <input
                    type="date"
                    placeholder="Purchase Date"
                    value={newVehicle.purchaseDate}
                    onChange={(e) => setNewVehicle({ ...newVehicle, purchaseDate: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Owner"
                    value={newVehicle.owner}
                    onChange={(e) => setNewVehicle({ ...newVehicle, owner: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Status"
                    value={newVehicle.status}
                    onChange={(e) => setNewVehicle({ ...newVehicle, status: e.target.value })}
                    required
                />
                <button type="submit">Add Vehicle</button>
            </form>
        </div>
    );
}

export default Vehicles;
