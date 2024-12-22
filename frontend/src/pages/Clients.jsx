// src/pages/Clients.js
import React, { useState, useEffect } from 'react';
import Header from '../components/Header'; 
import axios from 'axios'; // Import Axios for making HTTP requests
import './Clients.css'; 

function Clients() {
    const [clients, setClients] = useState([]);
    const [newClient, setNewClient] = useState({
        idCard: '',
        lastName: '',
        firstName: '',
        address: '',
        phone: '',
        email: ''
    });

    const [editClient, setEditClient] = useState(null);

    // Fetch clients data from the backend when the component mounts
    useEffect(() => {
        axios.get('http://localhost:8081/api/clients')  // Replace with your actual backend URL
            .then(response => {
                setClients(response.data);
            })
            .catch(error => {
                console.error('There was an error fetching the clients!', error);
            });
    }, []);

    const handleAddClient = () => {
        axios.post('http://localhost:8081/api/clients', newClient) // POST to add new client
            .then(response => {
                setClients([...clients, response.data]); // Add new client to the state
                setNewClient({
                    idCard: '',
                    lastName: '',
                    firstName: '',
                    address: '',
                    phone: '',
                    email: ''
                });
            })
            .catch(error => {
                console.error('There was an error adding the client!', error);
            });
    };

    const handleEditClient = () => {
        axios.put(`http://localhost:8081/api/clients/${editClient.id}`, editClient) // PUT to update client
            .then(response => {
                setClients(clients.map(client => client.id === editClient.id ? response.data : client));
                setEditClient(null);
            })
            .catch(error => {
                console.error('There was an error updating the client!', error);
            });
    };

    const handleDeleteClient = (id) => {
        axios.delete(`http://localhost:8081/api/clients/${id}`) // DELETE to remove client
            .then(() => {
                setClients(clients.filter(client => client.id !== id));
            })
            .catch(error => {
                console.error('There was an error deleting the client!', error);
            });
    };

    return (
        <div>
            <Header />
            <h1>Clients</h1>
            <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>ID Card</th>
                        <th>Last Name</th>
                        <th>First Name</th>
                        <th>Address</th>
                        <th>Phone</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {clients.map((client) => (
                        <tr key={client.id}>
                            <td>{client.id}</td>
                            <td>{client.idCard}</td>
                            <td>{client.lastName}</td>
                            <td>{client.firstName}</td>
                            <td>{client.address}</td>
                            <td>{client.phone}</td>
                            <td>{client.email}</td>
                            <td>
                                <button onClick={() => setEditClient(client)}>Edit</button>
                                <button onClick={() => handleDeleteClient(client.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <h2>Add New Client</h2>
            <form onSubmit={(e) => { e.preventDefault(); handleAddClient(); }}>
                <input type="text" placeholder="ID Card" value={newClient.idCard} onChange={(e) => setNewClient({ ...newClient, idCard: e.target.value })} required />
                <input type="text" placeholder="Last Name" value={newClient.lastName} onChange={(e) => setNewClient({ ...newClient, lastName: e.target.value })} required />
                <input type="text" placeholder="First Name" value={newClient.firstName} onChange={(e) => setNewClient({ ...newClient, firstName: e.target.value })} required />
                <input type="text" placeholder="Address" value={newClient.address} onChange={(e) => setNewClient({ ...newClient, address: e.target.value })} required />
                <input type="text" placeholder="Phone" value={newClient.phone} onChange={(e) => setNewClient({ ...newClient, phone: e.target.value })} required />
                <input type="email" placeholder="Email" value={newClient.email} onChange={(e) => setNewClient({ ...newClient, email: e.target.value })} required />
                <button type="submit">Add Client</button>
            </form>

            {editClient && (
                <div>
                    <h2>Edit Client</h2>
                    <form onSubmit={(e) => { e.preventDefault(); handleEditClient(); }}>
                        <input type="text" placeholder="ID Card" value={editClient.idCard} onChange={(e) => setEditClient({ ...editClient, idCard: e.target.value })} required />
                        <input type="text" placeholder="Last Name" value={editClient.lastName} onChange={(e) => setEditClient({ ...editClient, lastName: e.target.value })} required />
                        <input type="text" placeholder="First Name" value={editClient.firstName} onChange={(e) => setEditClient({ ...editClient, firstName: e.target.value })} required />
                        <input type="text" placeholder="Address" value={editClient.address} onChange={(e) => setEditClient({ ...editClient, address: e.target.value })} required />
                        <input type="text" placeholder="Phone" value={editClient.phone} onChange={(e) => setEditClient({ ...editClient, phone: e.target.value })} required />
                        <input type="email" placeholder="Email" value={editClient.email} onChange={(e) => setEditClient({ ...editClient, email: e.target.value })} required />
                        <button type="submit">Save Changes</button>
                    </form>
                </div>
            )}
        </div>
    );
}

export default Clients;
