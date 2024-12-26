# # Garage Management Application

## Overview 🌐
The Garage Management Application is a microservices-based system designed to streamline the daily operations of an automotive repair workshop. It provides features for managing clients, their vehicles, and the workshop's schedule while ensuring effective communication through notifications and invoicing.

### Technologies Used 🛠️
- **Spring Boot** for backend development
- **ReactJS** for frontend development
- **PostgreSQL** as the database
- **Eureka Server** for service discovery
- **Istio** for service mesh and traffic management
- **Docker** for containerization
- **Kubernetes** for orchestration

## Microservices Architecture 🌐
The application is divided into several microservices, each responsible for a specific domain:

1. **Client Service**: Manages client information, including personal details such as identification number, name, address, phone number, and email.
2. **Vehicle Service**: Handles vehicle data, including attributes like VIN, license plate, brand, model, year, and ownership.
3. **Workshop Service**: Organizes the workshop's schedule and maintenance tasks, including start time, end time, descriptions, status, and the associated vehicle.
4. **Notification Service**: Sends notifications (email or SMS) to clients when their vehicle is scheduled for maintenance or after a repair is completed.
5. **Invoice Service**: Generates and sends invoices to clients for completed maintenance tasks.

The services communicate asynchronously where needed, utilizing RabbitMQ for event-based messaging, ensuring loose coupling and scalability.

## Dockerizing the Services 🛠️
Each service in the application is containerized using Docker. The setup includes:

- Dockerfiles for building images for each service.
- A `docker-compose.yml` file to define and run multi-container Docker applications. This file orchestrates the deployment of all services, enabling seamless inter-service communication in a local development environment.

## Orchestration with Kubernetes and Istio ⚙️
The application is deployed in a Kubernetes cluster, leveraging its capabilities for scalability and resilience. The deployment includes:

- YAML files for deploying the services, creating necessary ConfigMaps, Secrets, and Persistent Volumes.
- **Istio** service mesh integration to enhance observability, security, and traffic management. Key features include:
  - Canary and Blue-Green deployments for zero-downtime updates.
  - Monitoring, logging, and tracing for detailed insights into microservices interactions.
  - Secure communication using mutual TLS (mTLS).
  - Circuit breaking at the API Gateway to improve fault tolerance.

This architecture ensures a robust, scalable, and secure environment for the Garage Management Application.
