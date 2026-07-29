package com.example.banksys.client.interfaces.dto;

import com.example.banksys.client.domain.model.ClientStatus;

import java.util.Set;
import java.util.UUID;

public record ClientEmployeeResponse(
        UUID bankId,
        String firstName,
        String lastName,
        String fullNationalId,
        String email,
        Set<String> phoneNumbers,
        ClientStatus clientStatus
) {}
