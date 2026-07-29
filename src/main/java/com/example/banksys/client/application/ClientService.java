package com.example.banksys.client.application;

import com.example.banksys.client.domain.model.ClientStatus;
import com.example.banksys.client.interfaces.dto.ClientCreateRequest;
import com.example.banksys.client.interfaces.dto.ClientEmployeeResponse;
import com.example.banksys.client.interfaces.dto.ClientPublicResponse;
import com.example.banksys.client.domain.model.Client;
import com.example.banksys.client.domain.model.PhoneNumber;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientPublicResponse createClient(ClientCreateRequest request) {
        Client client = new Client(request.firstName(), request.lastName(), request.nationalId());
        if (request.email() != null && !request.email().isBlank()) {
            client.setEmail(request.email());
        }

        for (PhoneNumber phone : request.phoneNumbers()) {
            client.addPhoneNumber(phone);
        }

        client = clientRepository.save(client);
        return toPublicResponse(client);
    }

    @Transactional(readOnly = true)
    public ClientPublicResponse getClientPublic(UUID bankId) {
        Client client = clientRepository.findById(bankId).orElseThrow(() -> new RuntimeException("Client not found"));
        return toPublicResponse(client);
    }

    @Transactional(readOnly = true)
    public ClientEmployeeResponse getClientEmployee(UUID bankId) {
        Client client = clientRepository.findById(bankId).orElseThrow(() -> new RuntimeException("Client not found"));
        return toEmployeeResponse(client);
    }

    @Transactional(readOnly = true)
    public List<ClientPublicResponse> getAllClients() {
        return clientRepository.findAll().stream().map(this::toPublicResponse).collect(Collectors.toList());
    }

    private ClientPublicResponse toPublicResponse(Client client) {
        return new ClientPublicResponse(
                client.getBankId(),
                client.getFirstName(),
                client.getLastName(),
                client.getNationalId().getMasked(),
                client.getEmail(),
                client.getPhoneNumbers().stream().map(PhoneNumber::getNumber).collect(Collectors.toSet()),
                client.getStatus()
        );
    }

    private ClientEmployeeResponse toEmployeeResponse(Client client) {
        return new ClientEmployeeResponse(
                client.getBankId(),
                client.getFirstName(),
                client.getLastName(),
                client.getNationalId().getFullId(),
                client.getEmail(),
                client.getPhoneNumbers().stream().map(PhoneNumber::getNumber).collect(Collectors.toSet()),
                client.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public Client findClientById(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Transactional(readOnly = true)
    public List<ClientPublicResponse> searchClients(String firstName, String lastName) {
        boolean firstNameExists = firstName != null && !firstName.isBlank();
        boolean lastNameExists = lastName != null && !lastName.isBlank();
        if (!firstNameExists && !lastNameExists)
            return clientRepository.findAll().stream().map(this::toPublicResponse).collect(Collectors.toList());
        if (!firstNameExists)
            return clientRepository.findByLastNameIgnoreCase(lastName).stream().map(this::toPublicResponse).collect(Collectors.toList());
        if (!lastNameExists)
            return clientRepository.findByFirstNameIgnoreCase(firstName).stream().map(this::toPublicResponse).collect(Collectors.toList());
        return clientRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName).stream().map(this::toPublicResponse).collect(Collectors.toList());
    }

    public ClientPublicResponse updateClient(UUID clientId, String firstName, String lastName, String email) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        if (firstName != null) client.setFirstName(firstName);
        if (lastName != null) client.setLastName(lastName);
        if (email != null) client.setEmail(email);
        clientRepository.save(client);
        return toPublicResponse(client);
    }

    public ClientPublicResponse addClientPhone(UUID clientId, PhoneNumber phoneNumber) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        client.addPhoneNumber(phoneNumber);
        clientRepository.save(client);
        return toPublicResponse(client);
    }

    public ClientPublicResponse removeClientPhone(UUID clientId, PhoneNumber phoneNumber) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        client.removePhoneNumber(phoneNumber);
        clientRepository.save(client);
        return toPublicResponse(client);
    }

    public ClientPublicResponse changeClientStatus(UUID clientId, ClientStatus newStatus) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        client.setStatus(newStatus);
        clientRepository.save(client);
        return toPublicResponse(client);
    }

}
