package com.example.banksys.client.interfaces.dto;

import com.example.banksys.client.domain.model.ClientStatus;

import java.util.Set;
import java.util.UUID;

public record ClientPublicResponse(
        UUID bankId,
        String firstName,
        String lastName,
        String maskedNationalId,
        String email,
        Set<String> phoneNumbers,
        ClientStatus clientStatus
) {}
