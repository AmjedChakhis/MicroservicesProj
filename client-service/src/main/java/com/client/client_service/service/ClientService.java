package com.client.client_service.service;

import com.client.client_service.entity.Client;
import java.util.List;

public interface ClientService {
    Client addClient(Client client);
    Client updateClient(Long id, Client client);
    void deleteClient(Long id);
    List<Client> getAllClients();
    Client getClientById(Long id);

    boolean clientExists(Long id);
}
