package com.example.banksys.client.application;

import com.example.banksys.client.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    List<Client> findByFirstNameIgnoreCase(String firstName);

    List<Client> findByLastNameIgnoreCase(String lastName);

    List<Client> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
}
