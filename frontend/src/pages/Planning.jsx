import React, { useState, useEffect } from 'react';
import './Planning.css';
import Header from '../components/Header';

function Planning() {
    const [tasks, setTasks] = useState([]);
    const [newTask, setNewTask] = useState({
        startTime: '',
        endTime: '',
        description: '',
        status: 'Planned',
        vehicle: ''
    });

    useEffect(() => {
        // Fetch all maintenance tasks from the backend
        fetch('/api/maintenance-tasks')
            .then((response) => response.json())
            .then((data) => setTasks(data))
            .catch((error) => console.error('Error fetching tasks:', error));
    }, []);

    const handleAddTask = (e) => {
        e.preventDefault();
        fetch('/api/maintenance-tasks', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newTask)
        })
            .then((response) => response.json())
            .then((createdTask) => {
                setTasks([...tasks, createdTask]);
                setNewTask({
                    startTime: '',
                    endTime: '',
                    description: '',
                    status: 'Planned',
                    vehicle: ''
                });
            })
            .catch((error) => console.error('Error adding task:', error));
    };

    const handleChangeStatus = (taskId, newStatus) => {
        const taskToUpdate = tasks.find((task) => task.id === taskId);
        const updatedTask = { ...taskToUpdate, status: newStatus };

        fetch(`/api/maintenance-tasks/${taskId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedTask)
        })
            .then((response) => response.json())
            .then((updatedTask) => {
                const updatedTasks = tasks.map((task) =>
                    task.id === taskId ? updatedTask : task
                );
                setTasks(updatedTasks);
            })
            .catch((error) => console.error('Error updating task:', error));
    };

    return (
        <div className="planning">
            <Header />
            <h1>Workshop Planning</h1>
            <table border="1" className="tasks-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Start Time</th>
                        <th>End Time</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th>Vehicle</th>
                    </tr>
                </thead>
                <tbody>
                    {tasks.map((task) => (
                        <tr key={task.id}>
                            <td>{task.id}</td>
                            <td>{task.startTime}</td>
                            <td>{task.endTime}</td>
                            <td>{task.description}</td>
                            <td>
                                <select
                                    value={task.status}
                                    onChange={(e) => handleChangeStatus(task.id, e.target.value)}
                                >
                                    <option value="Planned">Planned</option>
                                    <option value="In Progress">In Progress</option>
                                    <option value="Completed">Completed</option>
                                </select>
                            </td>
                            <td>{task.vehicle}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <h2>Add New Task</h2>
            <form onSubmit={handleAddTask} className="task-form">
                <input
                    type="datetime-local"
                    placeholder="Start Time"
                    value={newTask.startTime}
                    onChange={(e) => setNewTask({ ...newTask, startTime: e.target.value })}
                    required
                />
                <input
                    type="datetime-local"
                    placeholder="End Time"
                    value={newTask.endTime}
                    onChange={(e) => setNewTask({ ...newTask, endTime: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Description"
                    value={newTask.description}
                    onChange={(e) => setNewTask({ ...newTask, description: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Vehicle"
                    value={newTask.vehicle}
                    onChange={(e) => setNewTask({ ...newTask, vehicle: e.target.value })}
                    required
                />
                <select
                    value={newTask.status}
                    onChange={(e) => setNewTask({ ...newTask, status: e.target.value })}
                    required
                >
                    <option value="Planned">Planned</option>
                    <option value="In Progress">In Progress</option>
                    <option value="Completed">Completed</option>
                </select>
                <button type="submit">Add Task</button>
            </form>
        </div>
    );
}

export default Planning;
